/*
 *  $Id$
 *  IzPack
 *  Copyright (C) 2002 Jan Blok (jblok@profdata.nl - PDM - www.profdata.nl)
 *
 *  File :               Console.java
 *  Description :        a Console.
 *  Author's email :     jblok@profdata.nl
 *  Author's Website :   http://www.profdata.nl
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.izforge.izpack.util;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import javax.swing.text.Document;
import javax.swing.text.Segment;

public class Console
{
	public static int INITIAL_WIDTH = 800;
	public static int INITIAL_HEIGHT = 600;
	
	public static void main(String[] args)
	{
        Runtime rt = Runtime.getRuntime();
        Process p = null;
        try 
        {

            /*
              Start a new process in which to execute the
              commands in cmd, using the environment in env
              and use pwd as the current working directory.
            */
            p = rt.exec(args);//, env, pwd);
			Console c = new Console(p);
	        System.exit(p.exitValue());
        }
        catch (IOException e) 
        {
            /*
              Couldn't even get the command to start.  Most likely 
              it couldn't be found because of a typo.
            */
            System.out.println("Error starting: " + args[0]);
            System.out.println(e);
        }
	}
	
	private StdOut so;
	private StdOut se;

	public String getOutputData()	
	{
		if (so != null)
		{
			return so.getData();
		}
		else
		{
			return "";
		}
	}
	public String getErrorData()	
	{
		if (se != null)
		{
			return se.getData();
		}
		else
		{
			return "";
		}
	}
	
	public Console(Process p)
	{
		JFrame frame = new JFrame();
		frame.setTitle("Console");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(screenSize.width/2 - INITIAL_WIDTH/2,screenSize.height/2 - INITIAL_HEIGHT/2);
		ConsoleTextArea cta = new ConsoleTextArea();
		JScrollPane scroll = new JScrollPane(cta);
		scroll.setPreferredSize(new Dimension(INITIAL_WIDTH,INITIAL_HEIGHT));
		frame.getContentPane().add(scroll);
		frame.pack();

        // From here down your shell should be pretty much
        // as it is written here!
        /*
          Start up StdOut, StdIn and StdErr threads that write 
          the output generated by the process p to the screen, and
          feed the keyboard input into p. 
        */
        so = new StdOut(p,cta);
        se = new StdOut(p,cta);
        StdIn si = new StdIn(p,cta);
        so.start();
        se.start();
        si.start();
        
        // Wait for the process p to complete.
        try 
        {
			frame.setVisible(true);
            p.waitFor();
        }
        catch (InterruptedException e) 
        {
            /*
              Something bad happened while the command was
              executing.
            */
            System.out.println("Error during execution");
            System.out.println(e);
        }
        
        /*
          Now signal the StdOut, StdErr and StdIn threads that the 
          process is done, and wait for them to complete.
        */
        try 
        {
            so.done();
            se.done();
            si.done();
            so.join();
            se.join();
            si.join();
        }
        catch (InterruptedException e) 
        {                
            // Something bad happend to one of the Std threads.
            System.out.println("Error in StdOut, StdErr or StdIn.");
            System.out.println(e);
        }
		frame.setVisible(false);
    }
}

class StdIn extends Thread
{
	private BufferedReader kb;
	private boolean processRunning;
	private PrintWriter op;

	public StdIn(Process p,ConsoleTextArea cta)
	{
		setDaemon(true);
		InputStreamReader ir = new InputStreamReader(cta.getIn());
		kb = new BufferedReader(ir);

		BufferedOutputStream os = new BufferedOutputStream(p.getOutputStream());
		op = new PrintWriter((new OutputStreamWriter(os)), true);
		processRunning = true;
	}

	public void run()
	{
		try
		{
			while (kb.ready() || processRunning)
			{
				if (kb.ready())
				{
					op.println(kb.readLine());
				}
			}
		}
		catch (IOException e)
		{
			System.err.println("Problem reading standard input.");
			System.err.println(e);
		}
	}

	public void done()
	{
		processRunning = false;
	}
}

class StdOut extends Thread
{
	private InputStreamReader output;
	private boolean processRunning;
	private ConsoleTextArea cta;
	private StringBuffer data;
	
	public StdOut(Process p,ConsoleTextArea cta)
	{
		setDaemon(true);
		output = new InputStreamReader(p.getInputStream());
		this.cta = cta;
		processRunning = true;
		data = new StringBuffer();
	}

	public void run()
	{
		try
		{
			/*
			 Loop as long as there is output from the process
			 to be displayed or as long as the process is still
			 running even if there is presently no output.
			*/
			while (output.ready() || processRunning)
			{

				// If there is output get it and display it.
				if (output.ready())
				{
					char[] array = new char[255];
					int num = output.read(array);
					if (num != -1)
					{
						String s = new String(array,0,num);
						data.append(s);
						SwingUtilities.invokeAndWait( new ConsoleWrite(cta, s) );
					}
				}
			}
		}
		catch (Exception e)
		{
			System.err.println("Problem writing to standard output.");
			System.err.println(e);
		}
	}

	public void done()
	{
		processRunning = false;
	}
	
	public String getData()
	{
		return data.toString();
	}
}


class ConsoleWrite implements Runnable {
    private ConsoleTextArea textArea;
    private String str;

    public ConsoleWrite(ConsoleTextArea textArea, String str) {
        this.textArea = textArea;
        this.str = str;
    }

    public void run() {
        textArea.write(str);
    }
};

class ConsoleWriter extends java.io.OutputStream {

    private ConsoleTextArea textArea;
    private StringBuffer buffer;

    public ConsoleWriter(ConsoleTextArea textArea) {
        this.textArea = textArea;
        buffer = new StringBuffer();
    }

    public synchronized void write(int ch) {
        buffer.append((char)ch);
        if(ch == '\n') {
            flushBuffer();
        }
    }

    public synchronized void write (char[] data, int off, int len) {
        for(int i = off; i < len; i++) {
            buffer.append(data[i]);
            if(data[i] == '\n') {
                flushBuffer();
            }
        }
    }

    public synchronized void flush() {
        if (buffer.length() > 0) {
            flushBuffer();
        }
    }

    public void close () {
        flush();
    }

    private void flushBuffer() {
        String str = buffer.toString();
        buffer.setLength(0);
        SwingUtilities.invokeLater(new ConsoleWrite(textArea, str));
    }
};

class ConsoleTextArea extends JTextArea implements KeyListener,DocumentListener 
{
    private ConsoleWriter console1;
    private ConsoleWriter console2;
    private PrintStream out;
    private PrintStream err;
    private PrintWriter inPipe;
    private PipedInputStream in;
    private java.util.Vector history;
    private int historyIndex = -1;
    private int outputMark = 0;

    public void select(int start, int end) {
	requestFocus();
	super.select(start, end);
    }

    public ConsoleTextArea()
    {
        super();
        history = new java.util.Vector();
        console1 = new ConsoleWriter(this);
        console2 = new ConsoleWriter(this);
        out = new PrintStream(console1);
        err = new PrintStream(console2);
        PipedOutputStream outPipe = new PipedOutputStream();
        inPipe = new PrintWriter(outPipe);
        in = new PipedInputStream();
        try 
        {
            outPipe.connect(in);
        } catch(IOException exc) {
            exc.printStackTrace();
        }
        getDocument().addDocumentListener(this);
        addKeyListener(this);
        setLineWrap(true);
        setFont(new Font("Monospaced", 0, 12));
    }
    
    
    synchronized void returnPressed() {
        Document doc = getDocument();
        int len = doc.getLength();
        Segment segment = new Segment();
        try {
            doc.getText(outputMark, len - outputMark, segment);
        } catch(javax.swing.text.BadLocationException ignored) {
            ignored.printStackTrace();
        }
        if(segment.count > 0) {
            history.addElement(segment.toString());
        }
        historyIndex = history.size();
        inPipe.write(segment.array, segment.offset, segment.count);
        append("\n");
        outputMark = doc.getLength();
        inPipe.write("\n");
        inPipe.flush();
        console1.flush();
    }
    
    public void eval(String str) {
        inPipe.write(str);
        inPipe.write("\n");
        inPipe.flush();
        console1.flush();
    }
    
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_BACK_SPACE || code == KeyEvent.VK_LEFT) {
            if(outputMark == getCaretPosition()) {
                e.consume();
            }
        } else if(code == KeyEvent.VK_HOME) {
           int caretPos = getCaretPosition();
           if(caretPos == outputMark) {
               e.consume();
           } else if(caretPos > outputMark) {
               if(!e.isControlDown()) {
                   if(e.isShiftDown()) {
                       moveCaretPosition(outputMark);
                   } else {
                       setCaretPosition(outputMark);
                   }
                   e.consume();
               }
           }
        } else if(code == KeyEvent.VK_ENTER) {
            returnPressed();
            e.consume();
        } else if(code == KeyEvent.VK_UP) {
            historyIndex--;
            if(historyIndex >= 0) {
                if(historyIndex >= history.size()) {
                    historyIndex = history.size() -1;
                }
                if(historyIndex >= 0) {
                    String str = (String)history.elementAt(historyIndex);
                    int len = getDocument().getLength();
                    replaceRange(str, outputMark, len);
                    int caretPos = outputMark + str.length();
                    select(caretPos, caretPos);
                } else {
                    historyIndex++;
                }
            } else {
                historyIndex++;
            }
            e.consume();
        } else if(code == KeyEvent.VK_DOWN) {
            int caretPos = outputMark;
            if(history.size() > 0) {
                historyIndex++;
                if(historyIndex < 0) {historyIndex = 0;}
                int len = getDocument().getLength();
                if(historyIndex < history.size()) {
                    String str = (String)history.elementAt(historyIndex);
                    replaceRange(str, outputMark, len);
                    caretPos = outputMark + str.length();
                } else {
                    historyIndex = history.size();
                    replaceRange("", outputMark, len);
                }
            }
            select(caretPos, caretPos);
            e.consume();
        }
    }
 
    public void keyTyped(KeyEvent e) {
        int keyChar = e.getKeyChar();
        if(keyChar == 0x8 /* KeyEvent.VK_BACK_SPACE */) {
            if(outputMark == getCaretPosition()) {
                e.consume();
            }
        } else if(getCaretPosition() < outputMark) {
            setCaretPosition(outputMark);
        }
    }
    
    public synchronized void keyReleased(KeyEvent e) {
    }

    public synchronized void write(String str) {
        insert(str, outputMark);
        int len = str.length();
        outputMark += len;
        select(outputMark, outputMark);
    }
    
    public synchronized void insertUpdate(DocumentEvent e) {
        int len = e.getLength();
        int off = e.getOffset();
        if(outputMark > off) {
            outputMark += len;
        }
    }
    
    public synchronized void removeUpdate(DocumentEvent e) {
        int len = e.getLength();
        int off = e.getOffset();
        if(outputMark > off) {
            if(outputMark >= off + len) {
                outputMark -= len;
            } else {
                outputMark = off;
            }
        }
    }

    public synchronized void postUpdateUI() {
        // this attempts to cleanup the damage done by updateComponentTreeUI
        requestFocus();
        setCaret(getCaret());
        select(outputMark, outputMark);
    }
    
    public synchronized void changedUpdate(DocumentEvent e) {
    }
    
    
    public InputStream getIn() {
        return in;
    }
    
    public PrintStream getOut() {
        return out;
    }
    
    public PrintStream getErr() {
        return err;
    }
    
};
