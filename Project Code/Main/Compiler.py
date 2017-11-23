"""
File compiles c code in a given file when executed
"""

# Imports necessary modules
import subprocess
import os


get_file = os.getcwd() + "/Files/WorkingFile/FileName.txt"
file_name = ""

# opens FileName.txt to obtain the name of the current working file
with open (get_file, 'r') as read_file:
    file_name = read_file.readline().strip()

# constructs directory of file to be built
file_to_compile = os.getcwd() + "/Files/" + file_name + ".c"
# constructs directory of output file
file_to_write = os.getcwd() + "/Files/compilerOutput.txt"

# compiles the given file
bash = subprocess.Popen(['gcc', file_to_compile, '-o', 'out'])
bash.communicate()
bash.wait()
bash = subprocess.Popen(['sudo', './out'], stdout=subprocess.PIPE)

# stores the terminal output (after compilatioin)
output = bash.stdout.read().decode('utf-8')

# writes the output to other file
with open (file_to_write, 'w') as save:
    save.write(output)
