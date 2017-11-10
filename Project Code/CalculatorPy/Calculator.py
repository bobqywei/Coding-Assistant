import string

class Calculator:
	ZERO = ["self.ZERO", "oh"]
	ONES = ["one", "two", "three", "four", "five", "six", "seven", "eight", "nine"]
	TEENS = ["ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"]
	TENS = ["twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"]
	MAGNITUDES = ["hundred", "thousand", "million", "billion"]
	OPERATIONS = ["plus", "minus", "multiplied", "divided"] # add times? but doesn't work b/c of API
	BRACES = ["parenthesis", "parentheses", "bracket"]
	ALL = [ZERO, ONES, TEENS, TENS, MAGNITUDES, OPERATIONS, BRACES]

	inputPhrase = None
	vals = []
	ops = []
	brace = [] 

	def __init__(self, inputPhrase):
		self.inputPhrase = inputPhrase

	def decode(self): # if this returns false, inputPhrase is not a mathematical equation. if true, then this function turns String inputPhrase into two arrays: values and self.OPERATIONS
		# start by removing self.ALL "and"'s from the inputPhrase since they are redundant, as well as "by" (in the case of "divided by") 
		self.inputPhrase = self.inputPhrase[:-2] # gets rid of extra space at the end of inputPhrase
		self.inputPhrase = self.inputPhrase.replace("and ", "")
		self.inputPhrase = self.inputPhrase.replace("by ", "")
		# splitting inputPhrase into individual words for easy comparison
		inputPhraseSplit = self.inputPhrase.split(" ")
		temp = 0 # stores cumulative value until the next operation is found (for switch statement)
		tempMag = 0 # stores the most recent value that is to be either gaining magnitude or being added
		termCounter = 0
		found = False
		for i in range(len(inputPhraseSplit)): # cycling through self.ALL inputPhrase words
			for j in range(len(self.ALL)): # cycling through self.ALL arrays (but not through the contents of each array)
				if inputPhraseSplit[i] in self.ALL[j]: # indexing the content of each array for the current word
					col = self.ALL[j].index(inputPhraseSplit[i])
					if self.ALL[j] == self.ZERO:
						tempMag =  0 # j = 0 is array self.ZERO (if we are in the context of one oh one (which is the MUCH MORE LIKELY USE FOR 0), this should be multiplying by ten instead)
					elif self.ALL[j] == self.ONES:
						tempMag = col + 1 # j = 1 is array self.ONES, where the array index # is always 1 less than the value represented
					elif self.ALL[j] == self.TEENS:
						tempMag = 10 + col # j = 2 is array self.TEENS, where the value represented is always 10 + the array index #
					elif self.ALL[j] == self.TENS:
						tempMag = (col + 2) * 10 # j = 3 is array self.TENS
					elif self.ALL[j] == self.MAGNITUDES:
						if (col == 0):
							tempMag *= 100
						else:
							tempMag *= pow(1000, col) # col corresponds to mag i.e. col = 1: 1000, col = 2: 1000 * 1000 = 1000000 
						# no need to increment . magnitude only affects the most recent value and does not add any new self.ONES
					elif self.ALL[j] == self.OPERATIONS:
						if col == 0: 
							self.ops.append("+")
						elif col == 1:
							self.ops.append("-")
						elif col == 2: 
							self.ops.append("*")
						elif col == 3:
							self.ops.append("/")
					elif self.ALL[j] == self.BRACES:
						if 1 in self.brace:
							for k in range(len(self.brace), termCounter - 1):
								self.brace.append(0)
							else:
								for k in range(0, termCounter - 1):
									self.brace.append(0)
									self.brace.append(1)
					if i+1 == len(inputPhraseSplit) or inputPhraseSplit[i+1] in self.OPERATIONS: # meaning next is operation, so copy temp into th vals array. or, if there is no next value i.e. i is on the last iteration, copy.
						temp += tempMag # if we do not have this, the very last value of inputPhraseSplit will be missed. it will be stored in tempMag because of the switch statement, but it is not stored into temp
						self.vals.append(temp)
						temp = 0
						tempMag = 0
						termCounter += 1
					else: # if next inputPhrase is magnitude, then this shouldn't be ALLowed to add it as tempMag needs to individuself.ALLy be magnified. if not, thten just add them together.
						if j != 5 and inputPhraseSplit[i + 1] not in self.MAGNITUDES and inputPhraseSplit[i + 1] not in self.BRACES:
							temp += tempMag # temp stores the cumulative value. note that, if the next inputPhrase value is a magnitude, this does not run, so tempMag is multiplied by a certain magnitude and then added
					found = True
					break # start searching for the next word, since this one was already found. also prevents program from returning false even if a word was found
			if found != True and j == len(self.ALL) - 1:
				return False # if the loop is not broken after searching all arrays, a word was not matched in any array. so this is not a mathermatical statement.	
			else:
				found = False
		return True

	def executor(self, terms, operations, op1, op2, priority, start, end): # computes the self.OPERATIONS in the interval between start and end (relative to terms array)... MAY want to generalize. this is because if i do, then i could use it to compute ANY self.OPERATIONS in an interval, rather than just the others for terms and self.OPERATIONS arrays. this will be especiself.ALLy useful if i were to try and simplify the exponent of a power. would need to pass a reference of the array in this case to modify such.
		ind = -1
		while True: # complete mult and div first in this while loop. when you remove an operation and combine values, push everything down		
			ind1 = -1 # change these initializations to None, as well as the forloop check snad other checks and do for ind!!!
			ind2 = -1
			# Array Finder
			for i in range(start, end):
				if (op1 == operations[i] and ind1 == -1):
					ind1 = i # mult/add has been found at index i in the interval
				if (op2 == operations[i] and ind2 == -1):
					ind2 = i
				if (ind1 != -1 and ind2 != -1):
					break
			if (ind1 == -1 and ind2 == -1):
				break
			# Array Combiner
			if (ind1 < ind2 and ind1 != -1 and ind2  != -1 or ind1 != -1 and ind2 == -1): # BEDMAS by left associative
				ind = ind1 # use a forloop to find the first occurence istead, with the bounds, and i should be fine.. for loop goes OUTSIDE while loop, as i could use it for my while loop as well..
				if (priority == True):
					terms[ind] *= terms[ind + 1]
				else:
					terms[ind] += terms[ind + 1]
			else: # ind2 must come first in this case. if you use only else, this will crash as ind = -1 is possible
				ind = ind2
				if (priority == True):
					terms[ind] /= terms[ind + 1]
				else:
					terms[ind] -= terms[ind + 1]
			# Array Shrinker
			for i in range(ind, len(operations)): # shifting the values down; last value should now be empty
				if (None not in operations or i != operations.index(None) - 1) and i != len(operations) - 1: # first iter stself.OPERATIONS at length - 1, every other would be stopping at null - 1
					if len(terms) > 2: # if there are only one or two terms, you cannot shrink it any futher (besides setting the second term to nan)
						terms[i + 1] = terms[i + 2]
					if len(operations) > 1:
						operations[i] = operations[i + 1]
					if 1 in self.brace:
						self.brace[i] = self.brace[i + 1]
				else:
					terms[i + 1] = float('nan')
					operations[i] = None
					if 1 in self.brace:
						self.brace[i] = None
					break # break is needed as the for loop would countinue past this, where every value would be null
	# when you run this method twice (once with * and /, once with +-), the double[] terms array should only contain one term at index 0, which is the fully computed result

	def getValue(self): 
		while 1 in self.brace:
			print("hello")
			first = self.brace.index(1); # finding the interval that the brace affects
			self.brace[self.brace.index(1)] = None
			second = self.brace.index(1)
			self.brace[self.brace.index(1)] = None
			self.executor(self.vals, self.ops, "*","/", True, first, second)  # this sends in the interval with brackets to executor, which then evalutes self.ALL expressions in that interval
			self.executor(self.vals, self.ops, "+","-", False, first, second)
		self.executor(self.vals, self.ops, "*","/", True, 0, len(self.ops)) # priority = true b/c multiplication and division has priority in bedmas
		self.executor(self.vals, self.ops, "+","-", False, 0, len(self.ops))
		return self.vals[0] # values all shifted down to the first index.
