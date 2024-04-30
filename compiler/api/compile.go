package api

import (
	"compiler/utils"
	"fmt"
	"io"
	"io/fs"
	"math/rand"
	"net/http"
	"os"
	"path/filepath"
	"strconv"
)

func CompileHandler(w http.ResponseWriter, req *http.Request) {
	if req.Method != "POST" {
		fmt.Fprint(w, "Wrong http method")
		return
	}

	buffer, err := io.ReadAll(req.Body)
	if err != nil {
		fmt.Fprint(w, err.Error())
		return
	}

	parameters := req.URL.Query()
	if !parameters.Has("filename") {
		fmt.Fprint(w, "Missing parameter filename")
		return
	}

	fileext := filepath.Ext(parameters.Get("filename"))

	file := "/tmp/" + strconv.Itoa(rand.Int()) + fileext
	err = os.WriteFile(file, buffer, fs.ModePerm)
	if err != nil {
		fmt.Fprint(w, err.Error())
		return
	}
	defer os.Remove(file)

	if compiler, ok := utils.CompilerByFileExtension[fileext]; !ok {
		fmt.Fprint(w, "No compiler found for fileextension "+fileext)
	} else {
		out, err := compiler.Execute(file)
		if err != nil {
			fmt.Fprint(w, err.Error())
			return
		}

		fmt.Fprint(w, out)
	}
}
