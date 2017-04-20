#!/usr/bin/python

### Benjamin Ferraro 
## Coding Challange - Polymath Ventures -- 4/19/17

import sqlite3 # import sqlDB Framework
import sys
import os
import xml.etree.ElementTree as ET

#######
# Functions
#######

def setUpPage(file):
	file.write("<!doctype html>\n")
	file.write("<html lang=\"en\">\n")
	file.write("<head>\n")
	file.write("	<meta charset=\"utf-8\"/>\n\n")
	file.write("	<title>Category Tree</title>\n")
	file.write("	<meta name=\"Ebay Tree\" content=\"Polymath Ventures\"/>\n")
	file.write("	<link rel=\"stylesheet\" href=\"style.css\">")
	file.write("</head>\n")
	file.write("<body>\n")
	file.write("<img id=\"picPoly\" src=\"poly.png\" />\n")
	file.write("<img id=\"picEbay\" src=\"ebay.png\" />\n")
	file.write("<h1 id=\"header\">Category Tree</h1>\n");

def closePage(file):
	file.write("</div>\n")
	file.write("</body>\n")
	file.write("</html>")

def PrintHTMLTreeStructure(rootChild):
	htmlFile.write("<li><div>" + str(rootChild[0]) + ", " + rootChild[1].encode('utf-8') + ", " + str(rootChild[2]) + ", " + str(rootChild[3]) + "</div>\n")

	## Gather rootID's children
	rootChildID = (rootChild[0],)
	c.execute("select ID, Name, Lvl, BestOfferEnabled  from Categories WHERE ParentID = ? AND ParentID != ID", rootChildID)
	children = c.fetchall()

	## If there is children, start a new list
	if children != []:			
		htmlFile.write("<ul>\n")
		## For all children, call this method which will print their children
		for child in children:
			PrintHTMLTreeStructure(child)
		htmlFile.write("</ul>\n")

	htmlFile.write("</li>\n")


#######
# START SCRIPT
#######

# Read in command line arguments
commandArgs = sys.argv

# Set up connection to database
connection = sqlite3.connect('EbayCategories.db')
c = connection.cursor() # Pointer to DB 

### Command argument checking
## Render a html page from SQLite DB
if "--rebuild" in commandArgs and "--render" not in commandArgs:
	xml = open("categories.xml",'ar')

	## Drop and Recreate table
	c.execute("DROP TABLE Categories"); 
	c.execute("CREATE TABLE Categories (ID varchar(255), Name varchar(255), Lvl int, ParentID varchar(255), BestOfferEnabled varchar(255))")

	## CALL TO Ebay's API GetCategories
	os.system('./getEbayCategories.sh > categories.xml')

	# Parse XML eBay Response
	tree = ET.parse('categories.xml')
	root = tree.getroot()

	## Parse all categories and insetr values into DB
	for category in root.find("{urn:ebay:apis:eBLBaseComponents}CategoryArray").findall("{urn:ebay:apis:eBLBaseComponents}Category"):
		ID = category.find("{urn:ebay:apis:eBLBaseComponents}CategoryID").text
		name = category.find("{urn:ebay:apis:eBLBaseComponents}CategoryName").text
		lvl = category.find("{urn:ebay:apis:eBLBaseComponents}CategoryLevel").text
		PID = category.find("{urn:ebay:apis:eBLBaseComponents}CategoryParentID").text

		## Not all categories have this value, set to false if they do not
		if len(category.findall("{urn:ebay:apis:eBLBaseComponents}BestOfferEnabled")) != 0: 
			bestOfferEnabled = "true"
		else:
			bestOfferEnabled = "false"

		## Insert Category
		insertIntoDB = [ID, name, lvl, PID, bestOfferEnabled]
		c.execute("INSERT INTO Categories VALUES (?,?,?,?,?)", insertIntoDB)

	## Commit and close DB updates/connection
	connection.commit()
	connection.close()

	## Delete temporary XML
	os.system("rm categories.xml")


## Rebuild category tree structure from ebay API call
elif "--render" in commandArgs and "--rebuild" not in commandArgs:
	ID = (commandArgs[2],)
	c.execute("select ID, Name, lvl, BestOfferEnabled from Categories WHERE ID = ?", ID)
	error =  c.fetchone()
	
	## No Such CategoryID
	if error == None:
		exit("No category with ID: " + commandArgs[2])

	## Create .html file
	htmlName = commandArgs[2] + ".html"
	htmlFile = open(htmlName,'w')

	## Creates HTML skeleton code as well as headers/CSS style ref
	setUpPage(htmlFile)

	### Print inital root list and set up DIV for CSS
	htmlFile.write("<div class=\"tree\">\n")
	htmlFile.write("<ul>\n")

	### Recursive Fucntion that prints out the HTML tree structure
	PrintHTMLTreeStructure(error)

	### Close inital list
	htmlFile.write("</ul>\n")

	## Write in closing body and html tags
	closePage(htmlFile)

else:
	### Must contain at least one of the build in commands
	exit("Must Contain --render or --rebuild command! AND not both");
