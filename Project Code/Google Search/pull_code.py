import urllib.request
from bs4 import BeautifulSoup
import subprocess


def get_url_result(string_input):
	query = string_input.replace(" ", "+") + "+program+in+C+tutorialspoint"
	result_url = ""

	html_res = urllib.request.urlopen('https://www.bing.com/search?q=' + query).read()
	soup = BeautifulSoup(html_res, "html.parser")

	for link in soup.find_all('a', href=True):
		if link['href'].startswith("https://www.tutorialspoint.com"): #and link['href'].endswith("in_c.htm"):
			result_url = (link['href'])
			break

	print(result_url + "\n")

	return result_url


def get_save_code(result_url, filename):
	html_res = urllib.request.urlopen(result_url).read()
	soup = BeautifulSoup(html_res, "html.parser")

	desc = soup.find('p').getText()
	print(desc + "\n")

	code = soup.find('pre', {'class': "prettyprint"}).getText()
	code = code.replace('&plus;', '+')
	print(code)

	with open(filename, 'a+') as file:
		file.write(code)


def compile_in_terminal(filename):
	bash = subprocess.Popen(['C:/Windows/System32/bash'], stdin=subprocess.PIPE, stdout=subprocess.PIPE)
	input = 'gcc ' + filename + '\n'
	bash.stdin.write(input.encode())
	stdout = bash.communicate(b'./a.out')[0].decode('ascii')
	print(stdout)

if __name__ == '__main__':
    input = "show me an example of quick sort"
    file_name = input.replace(" ", "_") + ".c"

    url_result = get_url_result(input)
    get_save_code(url_result, file_name)
    run_in_terminal(file_name)
