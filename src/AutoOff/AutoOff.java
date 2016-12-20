/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AutoOff;

/**
 *
 * @author thehaohcm
 */
public class AutoOff implements Runnable{
    Thread thread;
    int time;
    AutoOffInterface autoOff;
    public AutoOff(Thread thread,AutoOffInterface autoOff,int time){
        this.thread=thread;
        this.autoOff=autoOff;
        this.time=time;
    }

    @Override
    public void run() {
        try{
            Thread.sleep(time);
            autoOff.closeConnect();
//            thread.interrupt();
            thread.stop();
            autoOff=null;
            thread=null;
            
//            System.out.println("Da thoat thread");
        }catch(Exception ex){
            System.out.println("Da co loi xay ra, Thread khong the tat");
        }
        
    }
    
}
