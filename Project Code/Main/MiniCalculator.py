import string
from decimal import Decimal

class MiniCalculator:
	ZERO = ["zero", "oh"]
	ONES = ["one", "two", "three", "four", "five", "six", "seven", "eight", "nine"]
	TEENS = ["ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"]
	TENS = ["twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"]
	MAGNITUDES = ["hundred", "thousand", "million", "billion"]

	inputPhrase = None

	def __init__(self, inputPhrase=None):
		if inputPhrase!=None:
			self.inputPhrase = inputPhrase

	def newInput(self, inputPhrase):
		self.inputPhrase = inputPhrase

	def decode(self):
		self.inputPhrase = self.inputPhrase.replace(" and ", " ")
		inputPhraseSplit = self.inputPhrase.split(" ")
		cumulativeValue = 0 
		tempMag = 0
		insertMode = False

		checkLength = len(inputPhraseSplit)-1
		if "point" in inputPhraseSplit:
			checkLength = inputPhraseSplit.index("point")
		if not set(inputPhraseSplit[:checkLength]).isdisjoint(self.ZERO):
			insertMode = True
		else:
			for i in range(checkLength):
				if inputPhraseSplit[i] in self.ONES and inputPhraseSplit[i+1] in self.ONES:
					insertMode = True
					break

		for i in range(len(inputPhraseSplit)):
			if inputPhraseSplit[i] == "point": #everthing after the point should be said as single digits
				decimalPlace = 1
				for j in range(i+1, len(inputPhraseSplit)):
					if inputPhraseSplit[j] in self.ONES:
						cumulativeValue+=Decimal(self.ONES.index(inputPhraseSplit[j])+1)/Decimal(pow(10,decimalPlace)) #solves floating point errors
					decimalPlace+=1
				break
			else:
				if insertMode == True: #IF THEY HAVE A ZERO ANYWHERE or if two "ones" are beside one another anywhere, THEY LIEKLY MEAN TO SAY DIGITS ONE BY ONE 
					cumulativeValue*=10
					if inputPhraseSplit[i] in self.ONES: 
						cumulativeValue+=self.ONES.index(inputPhraseSplit[i])+1
				else:
					if inputPhraseSplit[i] in self.ONES:
						col = self.ONES.index(inputPhraseSplit[i])
						tempMag += col + 1 # j = 1 is array self.ONES, where the array index # is always 1 less than the value represented
					elif inputPhraseSplit[i] in self.TEENS:
						col = self.TEENS.index(inputPhraseSplit[i])
						tempMag += 10 + col # j = 2 is array self.TEENS, where the value represented is always 10 + the array index #
					elif inputPhraseSplit[i] in self.TENS:
						col = self.TENS.index(inputPhraseSplit[i])
						tempMag += (col + 2) * 10 # j = 3 is array self.TENS
					elif inputPhraseSplit[i] in self.MAGNITUDES:
						col = self.MAGNITUDES.index(inputPhraseSplit[i])
						if (col == 0):
							tempMag *= 100
						else:
							tempMag *= pow(1000, col)
					if i+1 == len(inputPhraseSplit): 
						cumulativeValue += tempMag
					elif inputPhraseSplit[i+1] not in self.MAGNITUDES: #case where we have a number with a magnitude after i.e. two hundred. we don't want to add it to the cumulative total just yet
						if inputPhraseSplit[i] in self.TENS and i+2 < len(inputPhraseSplit): #case where we have a joint number with a magnitude after i.e. twenty two thousand
							if inputPhraseSplit[i+2] not in self.MAGNITUDES:
								cumulativeValue += tempMag
								tempMag = 0
						elif inputPhraseSplit[i] == "hundred":  #there are two possible ways to use hundred: one is to get the "222" part of "22200" and another would be to get the "200" part of "1200"
							if i+2 < len(inputPhraseSplit):
								if inputPhraseSplit[i+2] not in self.MAGNITUDES:
									if i+3 < len(inputPhraseSplit):
									 	if inputPhraseSplit[i+3] not in self.MAGNITUDES:
												cumulativeValue += tempMag
												tempMag = 0
									else:
										cumulativeValue += tempMag 
										tempMag = 0	
							else: 
								cumulativeValue += tempMag 
								tempMag = 0				
						else: 
							cumulativeValue += tempMag 
							tempMag = 0
		return cumulativeValue