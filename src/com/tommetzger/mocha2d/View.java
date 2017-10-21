package com.tommetzger.mocha2d;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;





public class View extends Canvas implements Runnable
{
	private static final long serialVersionUID = 1L;
	
	private Thread thread;
	private KeyInput keyInput;
	private MouseInput mouseInput;
	
	private boolean isRunning;
	private boolean scenePresented;
	
	private int fps;
	private int nodeCount;
	
	
	public String title;
	public boolean showsFPS;
	public boolean showsNodeCount;
	public boolean isPaused = false;
	
	public Scene scene;
	
	
	
	
	public View ()
	{
		
	}
	
	
	
	
	public View (Scene scene)
	{
		this.scene = scene;
		this.scene.view = this;
		this.scene.imageObserver = this;
		this.scene.controllerView = this;
		keyInput = new KeyInput(this.scene);
		mouseInput = new MouseInput(this.scene);
		this.scene.didMoveToView();
		addKeyListener(keyInput);
		addMouseListener(mouseInput);
		addMouseMotionListener(mouseInput);
		this.start();
	}
	
	
	
	
	public void presentScene(Scene scene)
	{
		this.scene = scene;
		this.scenePresented = true;
		this.scene.view = this;
		this.scene.imageObserver = this;
		this.scene.controllerView = this;
		keyInput = new KeyInput(this.scene);
		mouseInput = new MouseInput(this.scene);
		this.scene.didMoveToView();
		addKeyListener(keyInput);
		addMouseListener(mouseInput);
		addMouseMotionListener(mouseInput);
		this.start();
	}
	
	
	
	

	@Override
	public void run() 
	{
		if (scenePresented)
		{
			requestFocus();
			long lastTime = System.nanoTime();
			final double amountOfTicks = 60.0;
			double ns = 1000000000 / amountOfTicks;
			double delta = 0;
			int updates = 0;
			int frames = 0;
			long timer = System.currentTimeMillis();
			
			while(isRunning)
			{
				long now = System.nanoTime();
				delta += (now - lastTime) / ns;
				lastTime = now;
				
				if(delta >= 1)
				{
					this.tick();
					updates++;
					delta--;
				}
				
				frames++;
//				this.scene.update();
				this.render();
				
				if(System.currentTimeMillis() - timer > 1000)
				{
					timer += 1000;
					this.fps = frames;
//					System.out.println(updates + "Ticks, Fps" + frames);
					updates = 0;
					frames = 0;
				}
			}
			
			
			this.stop();
		}
	}
	
	
	
	
	private synchronized void start()
	{
		if(isRunning)
		{
			return;
		}
		
		
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}
	
	
	
	
	private synchronized void stop()
	{
		if(!isRunning)
		{
			return;
		}
		
		isRunning = false;
		
		try 
		{
			thread.join();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	
	
	private void tick()
	{
//		System.out.println("View Tick!");
		if (!scene.realChildren.isEmpty())
		{
			LinkedList<Node> fauxChildren = this.scene.realChildren;
//			LinkedList<Node> toBeRemoved = new LinkedList<Node>();
			for (Node node : fauxChildren) 
			{
//				if(node.shouldBeRemoved)
//				{
////					scene.realChildren.remove(node);
//					toBeRemoved.add(node);
//				}
//				else
//				{
					node.tick();
//				}
			}
			
//			this.scene.realChildren.removeAll(toBeRemoved);
			
			for (int i = 0; i < this.scene.realChildren.size(); i++) 
			{
				for (int j = i+1; j < this.scene.realChildren.size(); j++)
				{
					if(this.scene.realChildren.get(i).hasPhysicsBody && this.scene.realChildren.get(j).hasPhysicsBody)
					{
						if(this.scene.realChildren.get(i).getPhysicsBody().body.intersects(this.scene.realChildren.get(j).getPhysicsBody().body))
					    {
//							System.out.println("Contact!");
					    	PhysicsContact contact = new PhysicsContact(this.scene.realChildren.get(i).getPhysicsBody(), this.scene.realChildren.get(j).getPhysicsBody());
					    	this.scene.didBeginContact(contact);
					    }
					}
				}
			}
		}
		
		
		this.scene.tick();
		
		
		nodeCount = scene.realChildren.size(); // TODO: doesn't get count children's children
	}
	
	
	
	
	private void render()
	{
		BufferStrategy bufferStrategy = this.getBufferStrategy();
		
		if(bufferStrategy == null)
		{
			createBufferStrategy(3); //may need number other than 3
			return;
		}
		
		Graphics2D graphics = (Graphics2D)bufferStrategy.getDrawGraphics();
		
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
		graphics.setColor(Color.BLACK);
		
		graphics.setRenderingHint(
		        RenderingHints.KEY_TEXT_ANTIALIASING,
		        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	
		
		if (!scene.realChildren.isEmpty())
		{
			LinkedList<Node> fauxChildren = this.scene.realChildren;
			LinkedList<Node> toBeRemoved = new LinkedList<Node>();
			for (Node node : fauxChildren) 
			{
				if(node.shouldBeRemoved)
				{
					toBeRemoved.add(node);
				}
				else
				{
					node.render(graphics);
				}
			}
			
			this.scene.realChildren.removeAll(toBeRemoved);
		}
		
		this.scene.render(graphics);
		
		if (this.showsFPS && this.showsNodeCount)
		{
			graphics.drawString("FPS: " + this.fps + ", Nodes: " + this.nodeCount, 5, this.getHeight() - 5);
		}
		else if (this.showsFPS)
		{
			graphics.drawString("FPS: " + this.fps, 5, this.getHeight() - 5);
		}
		else if (this.showsNodeCount)
		{
			graphics.drawString("Nodes: " + this.nodeCount, 5, this.getHeight() - 5);
		}
		
		
		graphics.dispose();
		bufferStrategy.show();
	}
}