from MiniCalculator import *
import os
# IMPORTANT WORDS: 'and' usually for new line. 'exit brace/if/loop' usually for exiting the current block and jumping to one level higher. 'exit print' to stop reading input for 'print'
class Functions: 
	OPERATORS = ["plus", "minus", "times", "divide", "divided", "mod", "modulo", "equal", "equals", "not", "lessthan", "greaterthan"]
	SYMBOLS = ["+", "-", "*", "/", "/", "%", "%", "=", "=", "!", "<", ">"]
	BOOLSYM = ["=", "!", "<", ">"]
	KEYWORDS = ["and", "or", "int", "double", "char", "if", "then", "else if", "elseif", "else", "print", "endprint", "break", "while"]
	LOGICKEYWORDS = ["and", "or"]
	DATAKEYWORDS = ["int", "double", "char"]
	FORMATTERS = ["%d", "%f", "%c"]
	IFKEYWORDS = ["if", "then", "else if", "elseif", "else"]
	PRINTKEYWORDS = ["print", "exitprint"]
	PRINTMODE = False # this allows us to read multiple lines for the same 'print' call until and 'exitprint' shows up
	EXITPRINTFORMAT = '"' # global variable that concantenates user declared variables if they are used in printf() statements. this is later concatenated and reset once the user is finished printing i.e. says 'exit print'
	INDENTKEYS = ["if", "while"] # these are keywords that create blocks of codes, i.e. the next lines will be indented
	NAMEWHITELIST = [] # this is a whitelist that builds... it contains only user-declared variables so we can use it later for printf() statements in which we need to match with the formatting signs
	DATAWHITELIST = []
	INDEPDATALIST = [] #i.e. "indep"endent data list, it is a list of user-declared variables that were not inited, they are handled in a separate way for indentations/semicolons
	fileName = ""
	indentCounter = 0 # counts number of '\t' in indents
	string = "" # input from Speech.txt
	stringArray = [] # this actively stores all previous code that was created by this class
	currentArray = None # this stores the current input in an array, and is to be translated/normalized
	iterator = 0 # counter that constantly builds across new inputs. is used at the end of each new input to mostly write to file the currentArray, and also to allow references to previously created code

	def __init__(self):
		pathToName = 'Files/WorkingFile/FileName.txt'
		with open(pathToName, 'r') as reader:
			self.fileName = os.path.join('Files', reader.read()[:-1] + '.c')

	# this runs everytime newInput() is called (incase the user changes files, and then decides to use this)
	def changeOutFile(self):
		pathToName = 'Files/WorkingFile/FileName.txt'
		with open(pathToName, 'r') as reader:
			self.fileName = os.path.join('Files', reader.read()[:-1] + '.c')

	# this is run by Main.py.. grabs input from Speech.txt
	def newInput(self):
		self.changeOutFile()
		
		with open('Files/Speech.txt', 'r') as f:
			self.string = f.read()
		
		# some common redundant words that we can immediately remove. note that we do not want to remove these from print _ exitprint statements however
		self.string = self.string.replace("raspberry ", "")
		self.string = self.string.replace(" called ", " ")
		self.string = self.string.replace(" named ", " ")
		self.string = self.string.replace(" to ", " ")
		self.string = self.string.replace(" is ", " ")
		self.string = self.string.replace(" by ", " ")
		self.string = self.string.replace(" be ", " ")
		self.currentArray = self.string.split(" ")
	
		oldLength = len(self.stringArray) # this is to help determine where in stringArray to start writing to file
	
		if self.string == "exit code ": # if we hear this, then we want to automatically create semicolons and } to end the file immediately
			
			if self.indentCounter == 0:
				self.stringArray.append(";")
			
			else: 
				while self.indentCounter > 0:
					self.exitCreator()
		
		if self.indentCounter > 0 and (self.string == "exit brace " or self.string == "exit loop " or self.string == "exit if "): # if we hear any of these, we want to close the current block, and start working on the next
			self.exitCreator()
		
		else:
			self.translate()
			self.filter()
			self.stringArray.extend(self.currentArray)
			self.format() 
		
		with open(self.fileName, 'a') as output:
			output.write(self.arrayToString(self.stringArray[oldLength:]))

	# this does two things: first, concatenates any key words that are more than one word so we can have them take up only a single spot in the currentArray instead
	# second, it changes all operators from words into symbols
	def translate(self):
		
		self.keywordConcatenator("elseif", "else", "if")
		self.keywordConcatenator("lessthan", "less", "than")
		self.keywordConcatenator("greaterthan", "greater", "than")
		self.keywordConcatenator("exitprint", "exit", "print")
		
		for i in range(len(self.OPERATORS)):
			while self.OPERATORS[i] in self.currentArray:
				self.currentArray[self.currentArray.index(self.OPERATORS[i])] = self.SYMBOLS[i]

	# filters uneccessary words in currentArray for preparation to be extended into stringArray
	def filter(self):
		while "integer" in self.currentArray:
			self.currentArray[self.currentArray.index("integer")] = "int"
		
		while "character" in self.currentArray:
			self.currentArray[self.currentArray.index("character")] = "char"

		if "or" in self.currentArray or not set(self.DATAKEYWORDS).isdisjoint(self.currentArray):
			j=0
			
			while j < len(self.currentArray):
				
				if self.currentArray[j] == "or" and self.currentArray[j+1] == "=" and (self.currentArray[j-1] == "<" or self.currentArray[j-1] == ">"):
					del self.currentArray[j]
					j-=1
				
				elif self.currentArray[j] in self.DATAKEYWORDS:
					self.DATAWHITELIST.append(self.currentArray[j])
					
					if "=" in self.currentArray[j:] and self.currentArray[j+2] == "=": 
						self.NAMEWHITELIST.append(self.currentArray[j+1])
						
						if self.currentArray[j] == "char":
							self.charInit(j+3)
						
						else: 
							self.numInit(j+3)
						
						j+=1
						continue
					
					elif ("it" in self.currentArray[j:] or "which" in self.currentArray[j:]):
					
						if "=" in self.currentArray[j+1:]: # we assume that 'it' refers to the var. if we have = somewhere in the distance then 'it' implies that the user wants to initialize. else, it just means something like "call it (varname)"
							self.NAMEWHITELIST.append(self.currentArray[j+1])
							
							if self.currentArray[j] == "char":
								self.charInit(j+self.currentArray[j:].index("=")+1)
							
							else:
								self.numInit(j+self.currentArray[j:].index("=")+1)
							inc = self.currentArray[j:].index("=")-1
							self.currentArray = self.currentArray[:j+2] + self.currentArray[j+self.currentArray[j:].index("="):] #we require the [j:] because what if multi var declaration
							j+=inc
					
						else:
							self.NAMEWHITELIST.append(self.currentArray[j+self.currentArray[j:].index("it")+1])
							self.INDEPDATALIST.append(self.currentArray[j+self.currentArray[j:].index("it")+1])	
				
					else:
						self.NAMEWHITELIST.append(self.currentArray[j+1])
						self.INDEPDATALIST.append(self.currentArray[j+1])
				
				j+=1

		# we add None's to ensure revelantOps doesn't crash. note this will automatically get filtered out in the next while loop
		self.currentArray.append(None) 
		self.currentArray.append(None)

		# stores every relevant word from self.currentArray into a tempArray, then we let self.currentArray = tempArray after we've gotten all relevant words
		tempArray = []
		ind = 0
		
		while (ind < len(self.currentArray)): 
			
			if self.currentArray[ind] == "exitprint":
				self.PRINTMODE = False
				self.EXITPRINTFORMAT+=")"
				tempArray.append(self.EXITPRINTFORMAT)
				self.EXITPRINTFORMAT='"'
				del self.currentArray[ind]
				ind-=1
			
			elif self.PRINTMODE == True and self.currentArray[ind] is not None:
				# we are assuming that if they try printing something and that 'thing' shares a name with a prev declared var, then they want the var
				if self.currentArray[ind] in self.NAMEWHITELIST:
					self.EXITPRINTFORMAT += ","
					self.EXITPRINTFORMAT += self.NAMEWHITELIST[self.NAMEWHITELIST.index(self.currentArray[ind])]
					self.currentArray[ind] = self.FORMATTERS[self.DATAKEYWORDS.index(self.DATAWHITELIST[self.NAMEWHITELIST.index(self.currentArray[ind])])] #replacing the word with the corresonding %thing
				
				tempArray.append(self.currentArray[ind])
			
			elif self.currentArray[ind] == "print":
				self.PRINTMODE = True
			
			if self.currentArray[ind] == "and": # figuring out which and to use (&&, 'and' for new line, or redundant)
				
				for j in range(ind, len(self.currentArray)):
			
					# if currently we see a boolean symbol, and next we see either a boolean symbol (unless current is just < or >), then we want to check backwards to make sure that there's another boolean operator. if so, we use && to join the two. else, it's for new line		
					if self.currentArray[j] in self.BOOLSYM and (self.currentArray[j+1] in self.BOOLSYM or self.currentArray[j] == "<" or self.currentArray[j] == ">"):
						tempCumulative = self.arrayToString(self.stringArray).split(" ")
						tempCumulative.extend(tempArray)
						i = len(tempCumulative)-1
						
						while i >= 0:
							if tempCumulative[i] in self.BOOLSYM and (tempCumulative[i-1] in self.BOOLSYM or tempCumulative[i] == "<" or tempCumulative[i] == ">"):
								tempArray.append("&&")
								break
							
							elif tempCumulative[i] in self.IFKEYWORDS or tempCumulative[i] in self.PRINTKEYWORDS or tempCumulative[i] in self.SYMBOLS:
								tempArray.append("and")
								break
							
							i-=1
						
						break	
					
					elif self.currentArray[j] in self.IFKEYWORDS or self.currentArray[j] in self.PRINTKEYWORDS or self.currentArray[j] in self.SYMBOLS:
						tempArray.append("and")
						break
			
			elif self.currentArray[ind] == "or":
					
				for j in range(ind, len(self.currentArray)):
					
					if self.currentArray[j] in self.BOOLSYM and (self.currentArray[j+1] in self.BOOLSYM or self.currentArray[j] == "<" or self.currentArray[j] == ">"):
						tempArray.append("||")
						break
					
					elif self.currentArray[j] in self.IFKEYWORDS or self.currentArray[j] in self.PRINTKEYWORDS or self.currentArray[j] in self.SYMBOLS:
						break
			
			# this catches all keywords and puts them into the tempArray
			elif (self.currentArray[ind] in self.KEYWORDS or (self.currentArray[ind] in self.INDEPDATALIST and self.currentArray[ind+1] != "=")):
				
				if (self.currentArray[ind] == "if" and "if" in self.currentArray[ind+1:] and "then" not in self.currentArray[ind:ind+self.currentArray[ind+1:].index("if")+1]) or (self.currentArray[ind] == "while" and "while" in tempArray and tempArray[-1] == "while"):
					ind+=1
					continue
				
				else:
					tempArray.append(self.currentArray[ind])
			
			# this catches all symbols, finds every var and number relevant to the symbol, and adds them into the tempArray
			elif (self.currentArray[ind] in self.SYMBOLS):

				if self.currentArray[ind] == "!" and self.currentArray[ind+1] != "=":
					tempArray.append("!")
				
				else:
					tempArray.append(self.currentArray[ind-1])
					end = self.revelantOps(ind) # end is the index of the last relevant word (relative to the operator at the index 'ind', e.g. a + b - c. if 'ind' is index of +, end is index of c)
					for j in range(ind, end+1):
						tempArray.append(self.currentArray[j]) # put everything between ind and end+1 into the array
					ind = end
			
			ind+=1
		
		self.currentArray = tempArray
		tempArray = None

	# iterates through the newly filtered and extended (into stringArray) currentArray to handle formatting (i.e. indentation, new line characters, proper syntax)
	def format(self):
		length = len(self.stringArray)
		
		while self.iterator < length:
			k = len(self.stringArray)
			
			if not set(self.KEYWORDS).isdisjoint(self.stringArray[self.iterator+1:]):
				
				for l in range(self.iterator+1, len(self.stringArray)):
					
					if self.stringArray[l] in self.KEYWORDS:
						k = l
						break
			
			if self.stringArray[self.iterator] in self.INDENTKEYS:
				self.indentCounter+=1 
				self.indents+='\t'
			
			if self.stringArray[self.iterator] == "and":
				if '\n' not in self.stringArray[self.iterator+1]:
					self.stringArray[self.iterator] = ';\n' + self.indents
				else:
					del self.stringarray[self.iterator]
					self.iterator-=1

			elif self.stringArray[self.iterator] in self.DATAKEYWORDS and self.stringArray[self.iterator+1] in self.INDEPDATALIST:
			
				if self.stringArray[:self.iterator] and "{" not in self.stringArray[self.iterator-1] and "}" not in self.stringArray[self.iterator-1]:
					self.stringArray.insert(self.iterator, ";\n" + self.indents)
					self.iterator+=1
					length+=1

			elif self.stringArray[self.iterator] in self.IFKEYWORDS:

				if self.stringArray[self.iterator] == "if": 
					self.stringArray[self.iterator] += " ("

				elif self.stringArray[self.iterator] == "elseif":
					self.stringArray[self.iterator] = ";\n" + self.indents[:-1] + "} else if ("

				elif self.stringArray[self.iterator] == "else":
					self.stringArray[self.iterator] = ";\n" + self.indents[:-1] + "} else {\n" + self.indents

				elif self.stringArray[self.iterator] == "then":
					self.stringArray[self.iterator] = ") {\n" + self.indents

			elif self.stringArray[self.iterator] == "print": # check if any vars. if yes, replace the var in the stringArray with the %sign, then add comma to end and do corressponding order of w/e
				self.stringArray[self.iterator] = 'printf("'
			
			elif self.stringArray[self.iterator] == "while":
				self.stringArray[self.iterator] += " ("
				self.stringArray.insert(k, ") {\n" + self.indents) # fix. what if i have not a keyword as my next line, but a+=b or something li that? ALSO FIX: WHY ARE IF STATEMTNS APPEARING TWICE??
				length+=1
			
			elif self.stringArray[self.iterator] == "!" and self.stringArray[self.iterator+1] != "=": #then it is for a boolean expr
				self.stringArray[self.iterator] += "("
				self.stringArray.insert(k, ")")
				length+=1
			
			self.iterator+=1

	# adds semi colons if neccessary, and adds } with proper indentation. then removes one tab value from indents (since when they call this, the only reason is to start writing in the next block)
	def exitCreator(self):
		if '\n' in self.stringArray[-1]: # if we already have tab values, then we also have semicolons. else, no tab values probably implies no semicolons (by the standard).. we may have the else case for stuff like if -> then-> and ->end brace->if
			self.stringArray[-1] = self.stringArray[-1][:-1] # remove one tab to properly place brace
			self.stringArray.append("}\n" + self.indents[:-1])
		
		else:
			self.stringArray.append(";\n" + self.indents[:-1] + "}\n" + self.indents[:-1])
		
		self.indentCounter-=1
		self.indents = self.indents[:-1]

	# formats initialized characters
	def charInit(self, indOfValue):
		temp = "'"
		temp+= self.currentArray[indOfValue]
		temp+= "'"
		self.currentArray[indOfValue] = temp

	# formats initialized integers and doubles (using MiniCalculator.py)
	def numInit(self, startIndOfValue):
		calc = MiniCalculator()
		preInputPhraseInd = startIndOfValue
		inputPhrase = ""
		i = startIndOfValue
		
		while i < len(self.currentArray):
			if self.currentArray[i] in self.SYMBOLS or (i+1 < len(self.currentArray) and self.currentArray[i] == "and" and self.currentArray[i+1] not in calc.ONES and self.currentArray[i+1] not in calc.TENS):
				
				if inputPhrase != "":
					calc.newInput(inputPhrase)
					temp = self.currentArray[i:]
					dec = i-preInputPhraseInd-1
					self.currentArray = self.currentArray[:preInputPhraseInd]
					self.currentArray.append(str(calc.decode()))
					self.currentArray.extend(temp)
					i-=dec
				
				if self.currentArray[i] in self.SYMBOLS:
					preInputPhraseInd = i+1
					inputPhrase = ""
				
				elif i+1 < len(self.currentArray) and self.currentArray[i] == "and" and self.currentArray[i+1] not in calc.ONES and self.currentArray[i+1] not in calc.TENS:
					break		
			
			elif self.currentArray[i] not in self.NAMEWHITELIST:
				inputPhrase+=str(self.currentArray[i] + " ")
			
			if i+1 == len(self.currentArray):
				calc.newInput(inputPhrase)
				temp = self.currentArray[i:]
				self.currentArray = self.currentArray[:preInputPhraseInd]
				self.currentArray.append(str(calc.decode()))
				self.currentArray.extend(temp)
				break
			
			i+=1

	# concatenate key words that are more than one word
	def keywordConcatenator(self, result, keyword1, keyword2):
		if keyword1 in self.currentArray: 
			
			for i in range(self.currentArray.index(keyword1), len(self.currentArray)-1):	
				
				if self.currentArray[i] == keyword1 and self.currentArray[i+1] == keyword2: # i.e. we have else in one index, and if in the other index. user probably wnats else if
					self.currentArray[i] = result
					del self.currentArray[i+1]
					
					if keyword1 in self.currentArray[i+1:]:
						i = self.currentArray[i+1:].index(keyword1) - 1
				
					else: 
						break

	# starting at the index of an operator, recursively searches forwards for more operators. base case is when next two indexes both aren't operators
	def revelantOps(self, index):
		if (self.currentArray[index+1] not in self.SYMBOLS and self.currentArray[index+2] not in self.SYMBOLS) or self.currentArray[index+2] is None: #if next two are not SYMBOLS, since we may have + 3 +, in which case we want to continue adding indices. note we are guarenteed that the second to last term will never be an operator, and the very last term is always \n.. HOWEVER, we may have the case where the user pauses and an operator is hanging.. ACCOUNT FOR THIS LATER
			return index+1 # base case: grab everything adjacent (will be vars) to this, since this is the last local operator.
		else:
			return self.revelantOps(index+1)

	# converts stringArray into a string (with formatting)
	def arrayToString(self, array):
		print(array)
		string = ""
		
		for i in range(len(array)): 
		
			if (i+1 != len(array) and (';' in array[i+1] or ';' in array[i] or ")" in array[i+1])) or i is len(array) - 1 or (array[i] in self.SYMBOLS and array[i+1][0] in self.SYMBOLS) or "(" in array[i] or "{" in array[i] or "}" in array[i]: # very first condition: if this is the last thing, we likely DO NOT want an extrra space. reasons: user is finished line, so extra space doesnt matter. but if user does not finish line, some complications: end with "plus", start with "equals"... once again another conflict is "equals" and "not" -> I THINK SEPARATING THIS IS EASY. JUST IGNORE CONCATENATION WHEN YOU SEE =!, AS ! BELONGS TO THE NEXT... BUT DON'T IGNROE != BECAUSE IT ALWAYS BELONGS TO =.. if this is not the last iterations, and if current and next element are operators, want to likely concatenate 
				string += array[i]
		
			else:
				string += array[i] + " "
		
		return string