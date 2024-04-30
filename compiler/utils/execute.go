package utils

import (
	"strings"
)

func Execute(command string) (string, error) {
	cmd := createCommand(command)
	var out strings.Builder
	cmd.Stdout = &out
	cmd.Stderr = &out

	err := cmd.Run()
	if err != nil {
		return "", err
	}

	return out.String(), nil
}
