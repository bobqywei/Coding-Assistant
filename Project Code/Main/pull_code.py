"""
	File contains functions for finding and obtaining code for various algorithms
"""

# imports necessary files
import text_to_speech as tts

# imports necessary modules
import urllib.request
from bs4 import BeautifulSoup
import subprocess


# function obtains url for a relevant website given a string input
def get_url_result(string_input):

	# formats the string for a query
	query = string_input.replace(" ", "+") + "+program+in+C"
	result_url = ""

	# posts a request to Bing and pulls entire HTML file of results page
	html_res = urllib.request.urlopen('https://www.bing.com/search?q=' + query).read()
	
	# Uses beautiful soup to parse HTML
	soup = BeautifulSoup(html_res, "html.parser")

	# finds all links in HTML
	for link in soup.find_all('a', href=True):
		
		# looks for specified links
		if link['href'].endswith("in_c.htm") or link['href'].startswith("https://www.tutorialspoint.com"):
			
			# stores the url
			result_url = (link['href'])
			break

	# returns the found url
	return result_url


# function gets and saves code from a webpage
def get_save_code(result_url, filename):

	# pulls HTML of webpage
	html_res = urllib.request.urlopen(result_url).read()
	# parses HTML
	soup = BeautifulSoup(html_res, "html.parser")

	# finds the first paragraph element and stores it
	desc = soup.find('p').getText()
	# sends the paragraph to text to speech
	tts.tts_and_play(desc)

	# pulls the code from the HTML
	code = soup.find('pre', {'class': "prettyprint"}).getText()
	# formats the code
	code = code.replace('&plus;', '+')

	# writes the code to the given file
	with open(filename, 'w') as file:
		file.write(code)


# function runs code in terminal
def run_in_terminal(filename):
	
	# compiles the c file 
	bash = subprocess.Popen(['sudo', 'gcc', filename])
	bash.communicate()
	bash.wait()

	# executes the executable
	bash = subprocess.Popen(['sudo', './tmp'], stdout=subprocess.PIPE)

	# output
	print(bash.stdout.read().decode('utf-8'))


# executes test code if run from main
if __name__ == '__main__':
	input = "show me merge sort"
	file_name = input.replace(" ", "_") + ".c"
	url_result = get_url_result(input)
	get_save_code(url_result, file_name)
	run_in_terminal(file_name)
