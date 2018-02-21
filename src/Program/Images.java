package Program;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.imageio.ImageIO;

public class Images implements Serializable{
	public static BufferedImage load(String imageName)
    {
        BufferedImage image;
        try
        {
        	System.out.println(imageName);
            image = ImageIO.read(Images.class.getResourceAsStream(imageName));
            BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics g = img.getGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
            image = img;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        return image;
    }
	public static void save(BufferedImage img){
		String path = Controller.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		path = path.substring(0,path.length()-4)+"src/Textures/_unknown"+GamePanel.randomNumber(1, 4)+".png";
		//System.out.println("path: "+path);
		File outputfile = new File(path);
		outputfile.mkdirs();
		try {
			ImageIO.write(img, "png", outputfile);
			//GamePanel.sleep(5000, 5000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static BufferedImage[][] cut(String imageName, int sliceWidth, int sliceHeight)
	{
		//System.out.println("Starting image cut for "+imageName);
		long temp = System.currentTimeMillis();
		BufferedImage sheet;
		try
		{
			sheet = ImageIO.read(Images.class.getResourceAsStream(imageName));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}

		int w = sheet.getWidth();
		int h = sheet.getHeight();

		int xSlices = w/sliceWidth;
		int ySlices = h/sliceHeight;

		BufferedImage[][] images = new BufferedImage[xSlices][ySlices];
		for (int x=0; x<xSlices; x++)
			for (int y=0; y<ySlices; y++)
			{
				BufferedImage img = new BufferedImage(sliceWidth, sliceHeight, BufferedImage.TYPE_INT_ARGB);
				Graphics g = img.getGraphics();
				g.drawImage(sheet, -x*sliceWidth, -y*sliceHeight, null);
				g.dispose();
				images[x][y] = img;
			}
		
		//System.out.println("Done cutting image for "+imageName+". Total time spent on cut = "+(System.currentTimeMillis()-temp));
		return images;
	}
}
