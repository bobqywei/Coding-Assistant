import subprocess
import os

file_to_compile = os.getcwd() + "/Files/main.c"
file_to_write = os.getcwd() + "/Files/compilerOutput.txt"

bash = subprocess.Popen(['sudo', 'gcc', file_to_compile, '-o', '/Files/tmp'])
bash.communicate()
bash.wait()
bash = subprocess.Popen(['sudo', './Files/tmp'], stdout=subprocess.PIPE)

output = bash.stdout.read().decode('utf-8')

with open (file_to_write, 'w') as save:
    save.write(output)
