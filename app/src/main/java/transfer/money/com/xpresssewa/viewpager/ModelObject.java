package transfer.money.com.xpresssewa.viewpager;


import transfer.money.com.xpresssewa.R;

public enum ModelObject {

    RED(R.string.app_name, R.layout.pager_first),
    BLUE(R.string.app_name, R.layout.pager_second),
    GREEN(R.string.app_name, R.layout.pager_three);

    private int mTitleResId;
    private int mLayoutResId;

    ModelObject(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}
