import os
import sys
import re

rootdir='.';
pat2=re.compile('''(?<=['"])/todo22''');

for root, subFolders, files in os.walk(rootdir):
    for filename in files:
        if filename.find(".html")!=-1 or filename.find(".js")!=-1 or filename.find(".jsp")!=-1:
            path=root+'/'+filename;
            with open(path,'r') as f:
                lines=f.readlines();

            with open(path,'w') as f:
                for line in lines:
                    line=pat2.sub(r'''/todo''',line);
                    f.write(line);

