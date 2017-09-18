import numpy as np
from PIL import Image
root = '/home/sourav/Butterfly/Butterfly'
cout = 0;
for d in sorted(os.listdir(root)):
    if(os.path.isdir(os.path.join(root,d))):
        for im in sorted(os.listdir(os.path.join(root,d))):
            ht = im;
            ht = "hflip" + im
            img = plt.imread(os.path.join(root,d,im))
            hplip = np.fliplr(img)
            hplipn = Image.fromarray(hplip)
            hplipn.save(os.path.join(root,d,ht))
            vt = im;
            vt = "vflip" + im
            vplip = np.flipud(img)
            vplipn = Image.fromarray(hplip)
            vplipn.save(os.path.join(root,d,vt))
