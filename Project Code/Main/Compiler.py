import subprocess
import os

file_to_compile = os.getcwd() + "/Files/main.c"
file_to_write = os.getcwd() + "/Files/compilerOutput.txt"

bash = subprocess.Popen(['gcc', file_to_compile, '-o', 'out'])
bash.communicate()
bash.wait()
bash = subprocess.Popen(['sudo', './out'], stdout=subprocess.PIPE)

output = bash.stdout.read().decode('utf-8')

with open (file_to_write, 'w') as save:
    save.write(output)
