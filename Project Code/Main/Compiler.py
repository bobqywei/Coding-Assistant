import subprocess
import os

get_file = os.getcwd() + "/Files/WorkingFile/FileName.txt"
file_name = ""
with open (get_file, 'r') as read_file:
    file_name = read_file.readline().strip()

file_to_compile = os.getcwd() + "/Files/" + file_name + ".c"
file_to_write = os.getcwd() + "/Files/compilerOutput.txt"

bash = subprocess.Popen(['gcc', file_to_compile, '-o', 'out'])
bash.communicate()
bash.wait()
bash = subprocess.Popen(['sudo', './out'], stdout=subprocess.PIPE)

output = bash.stdout.read().decode('utf-8')

with open (file_to_write, 'w') as save:
    save.write(output)
