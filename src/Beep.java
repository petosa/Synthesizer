//The Beep.java class has been adopted from code posted at:
//http://stackoverflow.com/questions/3780406/how-to-play-a-sound-alert-in-a-java-application
//This is how I discovered you could generate electronic tones in Java.

import javax.sound.sampled.*;

public class Beep {

  public static float SAMPLE_RATE = 8000f;

  //When called, plays a tone of the specified hertz for
  //the specified duration
  public static void tone(int hz, int msecs) 
     throws LineUnavailableException 
  {
     tone(hz, msecs, 1.0);
  }

  //When called, plays a tone of the specified hertz for
  //the specified duration at the specified volume
  public static void tone(int hz, int msecs, double vol)
      throws LineUnavailableException 
  {
    byte[] buf = new byte[1];
    AudioFormat af = 
        new AudioFormat(
            SAMPLE_RATE, // sampleRate
            8,           // sampleSizeInBits
            1,           // channels
            true,        // signed
            false);      // bigEndian
    SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
    sdl.open(af);
    sdl.start();
    
    //Increment the notesPlaying integer
    GUI.notesPlaying++;
    
    //If the 'silence' flag is not set to true, play the tone.
    for (int i=0; i < msecs*8; i++) {
    	if(!GUI.silence){
      double angle = i / (SAMPLE_RATE / hz) * 2.0 * Math.PI;
      buf[0] = (byte)(Math.sin(angle) * 127.0 * vol);
      sdl.write(buf,0,1);
    	}
    	else{sdl.stop(); i = msecs*8;}
    }
    sdl.drain();
    sdl.stop();
    sdl.close();
    
    //Decrement the notesPlaying integer
	GUI.notesPlaying--;

  }
  
}