package transfer.money.com.xpresssewa.interfaces;
public interface RxAPICallback<P> {
    void onSuccess(P t);

    void onFailed(Throwable throwable);
}

