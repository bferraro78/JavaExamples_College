Hello Polymath Ventures Team!

Running options

	- ./categories.py --rebuild
	- ./categories.py --render <category_id>

-- rebuild:
	Uses getEbayCategories.sh to make a call to eBay's GetCategories API. It stores thw XMl output in a temporary xml file. It then clears the table "Categories" in the 
	EbayCategories.db, then parses the XML and adds each categorie's value to the table.

	Category table: Categories (ID varchar(255), Name varchar(255), Lvl int, ParentID varchar(255), BestOfferEnabled varchar(255))

-- render: 
	Creates the <category_id>.html file and sets up the HTML skeleton code. Then, using the ParentID and CategeoryID from eBay's API stored in the database, a recursive function prints out the tree structure in HTML format.