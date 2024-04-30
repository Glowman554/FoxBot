package main

import (
	"compiler/api"
	"compiler/utils"
	"log"
	"net/http"
	"strconv"
)

var port = 1234

func main() {
	err := utils.EnsureNsjailInstalled()
	if err != nil {
		panic(err)
	}
	utils.CompilerByFileExtension[".py"] = utils.PythonInterpreter{}
	utils.CompilerByFileExtension[".c"] = utils.GccCompiler{}
	utils.CompilerByFileExtension[".sh"] = utils.BashInterpreter{}

	http.Handle("/api/run", http.HandlerFunc(api.RunHandler))
	http.Handle("/api/compile", http.HandlerFunc(api.CompileHandler))

	log.Println("Listening on port " + strconv.Itoa(port))
	http.ListenAndServe("0.0.0.0:"+strconv.Itoa(port), nil)
}
