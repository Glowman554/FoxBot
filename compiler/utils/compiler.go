package utils

var CompilerByFileExtension = make(map[string]Compiler)

type Compiler interface {
	Execute(file string) (string, error)
}

// IMPLEMENTATIONS

type GccCompiler struct{}
type PythonInterpreter struct{}
type BashInterpreter struct{}
type FireStormCompiler struct{}

func (gcc GccCompiler) Execute(file string) (string, error) {
	_, err := Execute("gcc " + file + " -o " + file + ".elf")
	if err != nil {
		return "", err
	}
	return ExecuteJail(file + ".elf")
}

func (python PythonInterpreter) Execute(file string) (string, error) {
	return ExecuteJail("python3 " + file)
}

func (bash BashInterpreter) Execute(file string) (string, error) {
	return ExecuteJail("bash " + file)
}


func (fire FireStormCompiler) Execute(file string) (string, error) {
	_, err := Execute("fire compile --input=" + file + " --output=" + file + ".elf")
	if err != nil {
		return "", err
	}
	return ExecuteJail(file + ".elf")
}
