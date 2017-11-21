import subprocess

bash = subprocess.Popen(['sudo', 'gcc', filename, '-o', 'tmp'])
bash.communicate()
bash.wait()
bash = subprocess.Popen(['sudo', './tmp'], stdout=subprocess.PIPE)

output = bash.stdout.read().decode('utf-8')