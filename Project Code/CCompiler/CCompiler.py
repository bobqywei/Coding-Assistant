import os
from subprocess import check_output

def compile(fileName): # returns the output of a C file in the current dir //also require error checking
	command = "gcc " + fileName
	os.system(command) # compiling the given C file name
	output = check_output('./a.out') # output var now stores the output of the C file, in byte format
	return output.decode('ascii') # converting the bytes to a string (which is guarenteed to be ASCII??)

if __name__ == '__main__':
	print(compile("hello.c"))