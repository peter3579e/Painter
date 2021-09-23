# Painter

### Tasks

### Implement a Painter app.
Painter app: Just like Microsoft Paint, the user is able to create simple art by using different color pencils, erasers, and shapes. No UI specification for this quiz, you can design your own layout, if you don't have any idea, you can reference PicCollage's "Doodle" feature.

link - https://en.wikipedia.org/wiki/Microsoft_Paint

Requirements:
- Create a custom view that extends from Android.View
- Pencil mode - Can draw lines or any shape by touching the screen
- Color picker - Pick a different color to draw
- Eraser mode - Erase pencil's drawing path.
- Able to Undo/Redo.
- Able to save the painting and opened it again to continue drawing.

### Overall Architecture

1. Canvas allow users to draw his thing. Therefore, creating a custom view where the user could drag the finger to draw the strokes is the first step. To achieves this, I decide to create a DrawView class which extends the View class.
2. For the Pencil mode, I created a data class called stroke with attributes like color, width and path. Each object of this class will represents every draw on the canvas. The width would decided by the value of rangeslider and the color is decided by Colorpicker.
3. To achieve eraser mode, I decide to use white color as the background of the canvas is white.
4. To keep the recode of each stoke, I created an ArrayList of type Stroke. Using Stack of type Stroke I PUSH the last index of the array to stack when undo and POP when redo.
5. For the save button, you can either choose restore and store an image. The image will be stroe or restore via contecntResolver from Bitmap.

### Major Components

Name | Description | 
--- | --- |
Canvas | The Canvas class holds the "draw" calls. To draw something, you need 4 basic components: A Bitmap to hold the pixels, a Canvas to host the draw calls (writing into the bitmap), a drawing primitive (e.g. Rect, Path, text, Bitmap), and a paint (to describe the colors and styles for the drawing). | 
Bitmap |  Bitmap class which has methods that allow us to manipulate pixels in the 2d coordinate system | 
Paint |  It is used to style a shape drawn by a canvas | 
Stack |  The Stack class represents a last-in-first-out (LIFO) stack of objects | 
ContentResolver |  This class provides applications access to the content model | 
View |  This class represents the basic building block for user interface components. A View occupies a rectangular area on the screen and is responsible for drawing and event handling | 

### Design Idea
By Implementing icons, the user can immediately knows what the button does. To avoid the UI is too complicated, I decided to combine store and restore functions in the same button.
