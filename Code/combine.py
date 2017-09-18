import json
import io

def line(filep):
	file=open(filep,"r")
	add=file.read()
	add = add.replace("\n}",'}')
	file.close()
	file=open(filep,"w")
	file.write(add)
	file.close() 

fname = 'dump2.json'
line(fname)
#print str(str(json_data).split(" ")[400:500])

with io.open(fname, encoding='unicode_escape') as json_data:
	d = json.load(json_data)
	#print d

# with open(fname) as f:
#     content = f.readlines()
# content = [x.strip() for x in content] 

# k=0
# for i in content:
# 	if '""' in i and '"",' not in i:
# 		print "-----------",k,i
# 		k+=1
# 	#print i