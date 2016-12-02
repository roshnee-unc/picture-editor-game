package a8;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;

@SuppressWarnings(value = {"serial" })
public class FramePuzzle extends JPanel implements KeyListener, MouseListener {

	private PictureView picture;
	private ArrayList<SubPictureView> puzzle;
	private SubPictureView blankTile;

	private static int SQUARE_WIDTH;
	private static int SQUARE_HEIGHT;

	public FramePuzzle(Picture p) {
		setLayout(new GridLayout(5, 5));

		picture = new PictureView(p.createObservable());
		SQUARE_WIDTH = picture.getWidth() / 5;
		SQUARE_HEIGHT = picture.getHeight() / 5;

		Picture blank = new PictureImpl(SQUARE_WIDTH, SQUARE_HEIGHT);
		for (int k = 0; k < blank.getWidth(); k++) {
			for (int l = 0; l < blank.getHeight(); l++) {
				blank.setPixel(k, l, new ColorPixel(.48,.686,.83));
			}
		}
		blankTile = new SubPictureView(4, 4, blank.createObservable());
		blankTile.addKeyListener(this);
		blankTile.addMouseListener(this);

		puzzle = new ArrayList<SubPictureView>();

		int i = 0;
		while (puzzle.size() <= 24) {
			int x = i % 5;
			int y = i / 5;
			if (i <= 23) {
				SubPictureView part = new SubPictureView(x, y,
						p.extract(new Coordinate(x * SQUARE_WIDTH, y * SQUARE_HEIGHT),
								new Coordinate(x * SQUARE_WIDTH + SQUARE_WIDTH, y * SQUARE_HEIGHT + SQUARE_HEIGHT))
								.createObservable());
				part.addMouseListener(this);
				puzzle.add(part);
			} else {
				puzzle.add(blankTile);
			}
			i++;

		}

		for (SubPictureView piece : puzzle) {
			add(piece);
		}

		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	//moves blank tile one over in direction of arrow key pressed
	//prints if invalid key is pressed
	//remember, tile cannot be moved towards border if at border (system notifies you)
	//reindexes all the tiles so they correspond to area on grid
	@Override
	public void keyPressed(KeyEvent e) {
		int blankIndex = puzzle.indexOf(blankTile);
		
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			if (blankTile.getY() == 0) {
				System.out.println("You can't move the pieces up.");
				break;
			}
			puzzle.get(blankIndex-5).setY(blankTile.getY());
			blankTile.setY(blankTile.getY() - 1);
			Collections.swap(puzzle, blankIndex, blankIndex - 5);
			update();
			break;
		case KeyEvent.VK_DOWN:
			if (blankTile.getY() == 4) {
				System.out.println("You can't move the pieces down.");
				break;
			}
			puzzle.get(blankIndex+5).setY(blankTile.getY());
			blankTile.setY(blankTile.getY() + 1);
			Collections.swap(puzzle, blankIndex, blankIndex + 5);
			update();
			break;
		case KeyEvent.VK_LEFT:
			if (blankTile.getX() == 0) {
				System.out.println("You can't move the pieces left.");
				break;
			}
			puzzle.get(blankIndex-1).setX(blankTile.getX());
			blankTile.setX(blankTile.getX() - 1);
			Collections.swap(puzzle, blankIndex, blankIndex - 1);
			update();
			break;
		case KeyEvent.VK_RIGHT:
			if (blankTile.getX() == 4) {
				System.out.println("You can't move the pieces right.");
				break;
			}
			puzzle.get(blankIndex+1).setX(blankTile.getX());
			blankTile.setX(blankTile.getX() + 1);
			Collections.swap(puzzle, blankIndex, blankIndex + 1);
			update();
			break;
		default:
			System.out.println("Invalid key typed.");
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
	
	
	//moves blank tile to where clicked
	//shifts all tiles to where old location of blank tile was (like in a row)
	//updates all tiles that were moved and blank tile
	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getComponent().getX();
		int y = e.getComponent().getY();

		int blankIndex = puzzle.indexOf(blankTile);
		int clickIndex = y * 5 + x;

		if (x == blankTile.getX() && y == blankTile.getY()) {
			System.out.println("YOU CLICKED THE BLANK TILE");
		} else if (x != blankTile.getX() && y != blankTile.getY()) {
			System.out.println("The tile you selected is not valid.");
		} else if (x == blankTile.getX()) {
			
			if (y > blankTile.getY()) {
				for (int i = blankIndex; i < clickIndex; i += 5) {
					puzzle.get(i+5).decreaseYByOne();
					Collections.swap(puzzle, i, i + 5);
				}
			} else {
				for (int i = blankIndex; i > clickIndex; i -= 5) {
					puzzle.get(i-5).increaseYByOne();
					Collections.swap(puzzle, i, i - 5);
				}
			}
			blankTile.setY(y);
			update();
		} else {
			if (x > blankTile.getX()) {
				for (int i = blankIndex; i < clickIndex; i++) {
					puzzle.get(i+1).decreaseXByOne();
					Collections.swap(puzzle, i, i + 1);
				}
			} else {
				for (int i = blankIndex; i > clickIndex; i--) {
					puzzle.get(i-1).increaseXByOne();
					Collections.swap(puzzle, i, i - 1);
				}
			}
			blankTile.setX(x);
			update();
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	
	
	//helper method that updates the screen
	//removes previous picture and uploads new one
	public void update() {
		this.removeAll();
		for (SubPictureView p : puzzle) {
			add(p);
		}
		validate();
	}

}
