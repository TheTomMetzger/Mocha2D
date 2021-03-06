/**
 * @file SpriteNode.java
 * @brief A Node that contains an image
 * @author Tom Metzger
 * @date April 2017
 */

package com.mocha2d;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;





public class SpriteNode extends Node
{
	private BufferedImage image = null;
	private BufferedImageLoader loader = new BufferedImageLoader();
	

	
	
	/// Empty SpriteNode constructor.
	/** Empty SpriteNode constructor. Sets position to (0,0)*/
	public SpriteNode()
	{
		
	}
	
	
	
	
	/// Position SpriteNode constructor.
	/** Position SpriteNode constructor. Sets position to (x,y)*/
	public SpriteNode(double x, double y)
	{
		this.position.x = x;
		this.position.y = y;	
	}
	
	
	
	
	/// Image SpriteNode constructor.
	/** Image SpriteNode constructor. Sets image to imageNamed*/
	public SpriteNode(String imageName)
	{
		try 
		{
			this.image = loader.loadImage(imageName);
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	/// Full SpriteNode constructor.
	/** Full SpriteNode constructor. Sets image to imageNamed and position to (x,y). */
	public SpriteNode(double x, double y, String imageNamed)
	{
		this.position.x = x;
		this.position.y = y;
		
		try 
		{
			this.image = loader.loadImage(imageNamed);
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	/// Changes the image of the SpriteNode to imageNamed.
	/** Changes the image of the SpriteNode to imageNamed. For use after the SpriteNode is instantiated. */
	public void setImage(String imageNamed)
	{
		try 
		{
			this.image = loader.loadImage(imageNamed);
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	public BufferedImage getImage()
	{
		return this.image;
	}
	
	
	
	
	void tick()
	{
//		this.position.x += velX;
		if(!this.realChildrenToAdd.isEmpty())
		{
			this.realChildren.addAll(this.realChildrenToAdd);
			this.realChildrenToAdd.clear();
		}
		
		this.children = realChildren.toArray(new Node[realChildren.size()]);
		
		if (!this.realChildren.isEmpty())
		{
			LinkedList<Node> fauxChildren = this.realChildren;
			LinkedList<Node> toBeRemoved = new LinkedList<Node>();
			for (Node node : fauxChildren) 
			{
				
				if (node.shouldBeRemoved)
				{
					toBeRemoved.add(node);
				}
				else
				{
					node.tick();
				}
			}
			
			this.realChildren.removeAll(toBeRemoved);
		}
		
		
		if (this.hasActions)
		{
			if (!this.actionsToAdd.isEmpty())
			{
				this.actions.addAll(this.actionsToAdd);
				this.actionsToAdd.clear();
			}
			LinkedList<Action> toBeRemoved = new LinkedList<Action>();
			for (Iterator<Action> childAction = actions.iterator(); childAction.hasNext();) 
			{
				Action action = childAction.next();
				if (!action.actionComplete)
				{
					action.tick();
				}
				else
				{
					toBeRemoved.add(action);
				}
			}
			this.actions.removeAll(toBeRemoved);
		}
				
		if (this.hasPhysicsBody)
		{
			this.getPhysicsBody().body= this.getBounds();
		}
		
		this.controllerView.nodesTicked++;
	}
	
	
	
	
	void render(Graphics graphics)
	{
		final Graphics2D g = (Graphics2D) graphics.create();
		
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		
		
		if (!this.realChildren.isEmpty())
		{
			LinkedList<Node> fauxChildren = this.realChildren;
			for (Node node : fauxChildren) 
			{
				node.render(g);
			}
		}
		
		
		g.drawImage(this.image, (int)this.position.x, (int)this.position.y, (int)this.size.width, (int)this.size.height, this.imageObserver);
		
		g.dispose();
	}
}
