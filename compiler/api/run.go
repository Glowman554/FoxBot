package api

import (
	"compiler/utils"
	"fmt"
	"io"
	"net/http"
)

func RunHandler(w http.ResponseWriter, req *http.Request) {
	if req.Method != "POST" {
		fmt.Fprint(w, "Wrong http method")
		return
	}

	buffer, err := io.ReadAll(req.Body)
	if err != nil {
		fmt.Fprint(w, err.Error())
		return
	}

	output, err := utils.ExecuteJail(string(buffer))
	if err != nil {
		fmt.Fprint(w, err.Error())
		return
	}

	fmt.Fprint(w, output)
}
