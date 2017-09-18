from bs4 import BeautifulSoup
import requests
import json
import os
import wikipedia as wk

website="http://en.wikipedia.org/wiki/"
directory="Butterfly"

def pre(f):
	return f[len("= "):] if f.startswith("= ") else f

def wiki_page(cate):
	wiki = wk.page(cate)
	content = wiki.content
	print(content)
	words = content.split()
	for i in range(len(words)):
		if (words[i]=="See" and words[i+1]=="also") or words[i]=="Gallery" or words[i]=="References":
			words=words[:i-1]
			break
	content = " ".join(words).split("==")
	content=["short"]+content
	content = {pre(content[2*i].strip().lower()):pre(content[2*i+1].strip()) for i in range(int(len(content)/2))}
	return content

#Alternate
def wiki_page_alt(cate):
	wiki = wk.page(cate)
	content = wiki.content
	print(content)
	words = content.split()
	for i in range(len(words)):
		if (words[i]=="See" and words[i+1]=="also") or words[i]=="Gallery" or words[i]=="References":
			words=words[:i-1]
			break
	content = " ".join(words)#.split("==")
	content={"Wikipedia": str(content)}
	# content = {pre(content[2*i].strip().lower()):pre(content[2*i+1].strip()) for i in range(int(len(content)/2))}
	return content


def kingdom(div):
	text = div.text.split()
	for i,word in enumerate(text):
		if word=="Binomial":
			text = text[:i]
			break
	for i,word in enumerate(text):
		if word=="classification":
			desc = text[:i-1]
			classify = text[i+1:]
			break
	del classify[-2]
	sci = {classify[2*i].strip()[:-1].lower():classify[2*i+1].strip() for i in range(int(len(classify)/2))}
	return " ".join(desc),sci

final=[]
error=[]
for categ in os.listdir(directory):
	for i,char in enumerate(categ):
		if char.isupper() and i>0:
			cate=categ[:i]+"_"+categ[i:].lower()
			sci_name = categ[:i]+" "+categ[i:]
	try:
		r = requests.get(website+cate)
		data = r.text
		soup = BeautifulSoup(data,"html.parser")
		div = soup.find("table", attrs={"class": "infobox biota"})

		data1,data2 = kingdom(div)
		data = {"stname":sci_name,"cmname":data1}
		data.update(data2)
		data.update(wiki_page(cate))

		final.append(data)
		with open('dump.json', 'a') as outfile:
			json.dump(data, outfile, indent=2)
		print(website+cate)#,"\n",data,"\n\n")
	except Exception:
		error.append(sci_name)
		print("SOME ERROR\t\t",website+cate)

with open("final.json", "w") as writeJSON:
	json.dump({"butterflies":final}, writeJSON)