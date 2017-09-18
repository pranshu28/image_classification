from PIL import Image
from resizeimage import resizeimage
resize_method = Image.ANTIALIAS
with open('new.jpg', 'r+b') as fd_img:
    with Image.open(fd_img) as image:
        img = Image.open(fd_img)
        img = img.resize((299, 299),Image.ANTIALIAS)
        img.save('test-image-contain.jpg', img.format)
        fd_img.close()
