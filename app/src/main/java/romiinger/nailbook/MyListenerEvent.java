package romiinger.nailbook;

public class MyListenerEvent {
    private OnMyEventListener mOnEventListener;

    public void setOnEventListener(OnMyEventListener listener) {
        mOnEventListener = listener;
    }

    public void doEvent(String message) {

        if (mOnEventListener != null)
            mOnEventListener.onEvent(message); // event result object :)
    }


    public interface OnMyEventListener {
        void onEvent(String message);
    }
}