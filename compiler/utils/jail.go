package utils

import (
	"fmt"
	"io/fs"
	"log"
	"math/rand"
	"os"
	"os/exec"
	"path/filepath"
	"strconv"
	"strings"
)

var nsjailConfig = []string{"-l /tmp/nsjail.log", "-Mo", "--user 0", "--group 99999", "--chroot /", "-T /boot/", "-T /dev/", "-T /mnt/", "-T /media/", "-t 600", "-T /proc/", "--keep_caps"}
var commandFilter = []string{"$", "(", ")", "'", "\"", "|", "<", ">", "`", "\\"}

var nsjailInstallScript = `
set -ex
(
    git clone https://github.com/google/nsjail.git /tmp/nsjail || true 
    cd /tmp/nsjail
    make
)
cp /tmp/nsjail/nsjail nsjail.elf
rm -rf /tmp/nsjail
`

func ExecuteJail(command string) (string, error) {
	err := EnsureNsjailInstalled()
	if err != nil {
		return "", err
	}

	for i := range commandFilter {
		if strings.Contains(command, commandFilter[i]) {
			return "", fmt.Errorf("Command contains unallowed character " + commandFilter[i])
		}
	}

	absolutPathNsjail, err := filepath.Abs("nsjail.elf")
	if err != nil {
		return "", err
	}

	actualCommand := "echo '" + command + "' | " + absolutPathNsjail + " " + strings.Join(nsjailConfig, " ") + " -- /bin/bash"

	file := "/tmp/" + strconv.Itoa(rand.Int()) + ".sh"
	err = os.WriteFile(file, []byte(actualCommand), fs.ModePerm)
	if err != nil {
		return "", err
	}
	defer os.Remove(file)

	cmd := createCommand("bash " + file)
	var out strings.Builder
	cmd.Stdout = &out
	cmd.Stderr = &out

	err = cmd.Run()
	if err != nil {
		return "", err
	}

	return out.String(), nil
}

func createCommand(command string) *exec.Cmd {
	log.Println("Executing command", command)

	tmp := strings.Split(command, " ")
	return exec.Command(tmp[0], tmp[1:]...)
}

func EnsureNsjailInstalled() error {
	if _, err := os.Stat("nsjail.elf"); err != nil {
		err = os.WriteFile("/tmp/nsjail.sh", []byte(nsjailInstallScript), fs.ModePerm)
		if err != nil {
			return err
		}

		cmd := createCommand("bash /tmp/nsjail.sh")
		cmd.Stdout = os.Stdout
		cmd.Stderr = os.Stderr
		err = cmd.Run()
		if err != nil {
			return err
		}
	}

	return nil
}
