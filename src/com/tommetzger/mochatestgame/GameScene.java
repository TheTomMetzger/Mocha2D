package com.tommetzger.mochatestgame;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.mocha2d.*;





public class GameScene extends Scene 
{
	
	public SpriteNode player;
	public LabelNode score;
	public SpriteNode enemy;
	
	public int velX = 0;
	boolean runAction = false;
	
	boolean upIsPressed = false;
	boolean downIsPressed = false;
	boolean leftIsPressed = false;
	boolean rightIsPressed = false;
	

	
	
	@Override
	public void didMoveToView() 
	{
		player = new SpriteNode("/spaceship.png");
		
		player.name = "player";
		
		player.position.x = this.getHeight() / 2;
		player.position.y = this.getWidth() / 2;
		
		player.size.width = 50;
		player.size.height = 50;
		
//		player.setPhysicsBody(new PhysicsBody(1, 0));
		
		this.addChild(player);
				
		
		score = new LabelNode("Score: ");
		
		score.setFont("res/anklepants.ttf");
		score.setFontSize(15);
		
		score.position.x = 5;
		score.position.y = 15;
		
		this.addChild(score);
		
		
		enemy = new SpriteNode("/spaceship.png");
		
		enemy.name = "enemy";
		
		enemy.position.x = 50;
		enemy.position.y = this.getWidth() / 2;
		
		enemy.size.width = 50;
		enemy.size.height = 50;
		
		enemy.setPhysicsBody(new PhysicsBody(3, 1));
		
		this.addChild(enemy);
	}
	
	
	
	
	@Override
	public void mouseDown(MouseEvent e)
	{
		// TODO Auto-generated method stub
		System.out.println("Mouse Down!");
	}




	@Override
	public void mouseDragged(MouseEvent e) 
	{
		// TODO Auto-generated method stub
		System.out.println("Mouse Dragged!");
	}




	@Override
	public void mouseUp(MouseEvent e) 
	{
		// TODO Auto-generated method stub
		System.out.println("Mouse Up!");
	}
	
	
	
	
	
	@Override
	public void keyDown(KeyEvent e) 
	{
		int key = e.getKeyCode();
		
		
		if(key == KeyEvent.VK_RIGHT)
		{
			rightIsPressed = true;
		}
		else if(key == KeyEvent.VK_LEFT)
		{
			leftIsPressed = true;
		}
		else if(key == KeyEvent.VK_UP)
		{
			upIsPressed = true;
		}
		else if(key == KeyEvent.VK_DOWN)
		{
			downIsPressed = true;
		}
	}

	
	
	
	@Override
	public void keyUp(KeyEvent e) 
	{
		int key = e.getKeyCode();
		
		
		if(key == KeyEvent.VK_RIGHT)
		{
			rightIsPressed = false;
		}
		else if(key == KeyEvent.VK_LEFT)
		{
			leftIsPressed = false;
		}
		else if(key == KeyEvent.VK_UP)
		{
			upIsPressed = false;
		}
		else if(key == KeyEvent.VK_DOWN)
		{
			downIsPressed = false;
		}
		else if (key == KeyEvent.VK_SPACE)
		{
			Rectangle missileRect = new Rectangle((int)(player.position.x + (player.size.width / 2) - 2), (int)player.position.y, 4, 15);
			ShapeNode missile = new ShapeNode(missileRect);
			
			missile.fillColor = Color.BLACK;
			missile.strokeColor = Color.ORANGE;
			
			missile.name = "missile";
			
			missile.setPhysicsBody(new PhysicsBody(1, 0));
//			missile.position.x = player.position.x;
//			missile.position.y = player.position.y;

			this.addChild(missile);
			
			Action action = Action.moveToY(-25);
			
			missile.runAction(action);
		}
	}




	@Override
	public void update() 
	{		
		if(rightIsPressed && upIsPressed)
		{
			Action action = Action.moveBy(3, -3);
			player.runAction(action);
		}
		else if(rightIsPressed && downIsPressed)
		{
			Action action = Action.moveBy(3, 3);
			player.runAction(action);
		}
		else if(leftIsPressed && upIsPressed)
		{
			Action action = Action.moveBy(-3, -3);
			player.runAction(action);
		}
		else if(leftIsPressed && downIsPressed)
		{
			Action action = Action.moveBy(-3, 3);
			player.runAction(action);
		}
		else if(rightIsPressed)
		{
			Action action = Action.moveByX(3);
			player.runAction(action);
		}
		else if(leftIsPressed)
		{
			Action action = Action.moveByX(-3);
			player.runAction(action);
		}
		else if(upIsPressed)
		{
			Action action = Action.moveByY(-3);
			player.runAction(action);
		}
		else if(downIsPressed)
		{
			Action action = Action.moveByY(3);
			player.runAction(action);
		}
	}




	@Override
	public void didBeginContact(PhysicsContact contact) 
	{
		PhysicsBody firstBody = contact.bodyA;
		PhysicsBody secondBody = contact.bodyB;
				
		if (firstBody.collisionBitMask == secondBody.categoryBitMask)
		{
			System.out.println("Kaboom!");
			secondBody.node.removeFromParent();
			firstBody.node.removeFromParent();
		}
		else if (secondBody.collisionBitMask == firstBody.categoryBitMask)
		{
			System.out.println("Kaboom!");
			firstBody.node.removeFromParent();
			secondBody.node.removeFromParent();
		}
	}
}
