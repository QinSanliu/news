package com.example.parting_soul.news.fragment.settings;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.example.parting_soul.news.R;
import com.example.parting_soul.news.bean.Settings;
import com.example.parting_soul.news.utils.CommonInfo;
import com.example.parting_soul.news.utils.LogUtils;

/**
 * Created by parting_soul on 2016/10/18.
 * settings fragment
 */

public class SettingsPreferenceFragment extends PreferenceFragment
        implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
    private ListPreference mLanguagePreference;

    private ListPreference mFontPreference;

    private CheckBoxPreference mNightModePreference;

    private CheckBoxPreference mExitBeSurePreference;

    private CheckBoxPreference mNoPicModePreference;

    private Preference mClearPicCachePreference;

    private Preference mClearAllCachePreference;

    private Settings mSettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(Settings.SETTINGS_XML_NAME);
        //添加对应的Settings布局
        addPreferencesFromResource(R.xml.settings);

        mSettings = Settings.newsInstance();

        mLanguagePreference = (ListPreference) findPreference(Settings.LANUAGE_KEY);
        mFontPreference = (ListPreference) findPreference(Settings.FONT_SIZE_KEY);
        mNightModePreference = (CheckBoxPreference) findPreference(Settings.IS_NIGHT_KEY);
        mExitBeSurePreference = (CheckBoxPreference) findPreference(Settings.BACK_BY_TWICE_KEY);
        mNoPicModePreference = (CheckBoxPreference) findPreference(Settings.NO_PICTURE_KEY);
        mClearPicCachePreference = findPreference(Settings.CLEAR_PIC_CACHE);
        mClearAllCachePreference = findPreference(Settings.CLEAR_ALL_CACHE);
        if (mLanguagePreference != null && mFontPreference != null && mNightModePreference != null
                && mExitBeSurePreference != null && mNoPicModePreference != null
                && mClearPicCachePreference != null && mClearAllCachePreference != null) {

        }

        mNightModePreference.setChecked(Settings.is_night_mode);
        mNoPicModePreference.setChecked(Settings.is_no_picture_mode);
        mExitBeSurePreference.setChecked(Settings.is_back_by_twice);

        mLanguagePreference.setOnPreferenceChangeListener(this);
        mLanguagePreference.setOnPreferenceClickListener(this);
        mFontPreference.setOnPreferenceClickListener(this);
        mFontPreference.setOnPreferenceChangeListener(this);
        mNightModePreference.setOnPreferenceChangeListener(this);
        mNightModePreference.setOnPreferenceClickListener(this);
        mNoPicModePreference.setOnPreferenceClickListener(this);
        mNoPicModePreference.setOnPreferenceChangeListener(this);
        mClearPicCachePreference.setOnPreferenceClickListener(this);
        mClearAllCachePreference.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mLanguagePreference) {
            LogUtils.d(CommonInfo.TAG, "mLanguagePreference " + mLanguagePreference.getValue());
            if (mLanguagePreference.getValue().equals(newValue)) {

            }
            return true;
        } else if (preference == mFontPreference) {
            LogUtils.d(CommonInfo.TAG, "mFontPreference " + mFontPreference.getValue());
            return true;
        } else if (preference == mNightModePreference) {
//            LogUtils.d(CommonInfo.TAG, "mNightModePreference " + mNightModePreference.isChecked());
            Settings.is_night_mode = Boolean.parseBoolean(newValue.toString());
            mSettings.putBoolean(Settings.IS_NIGHT_KEY, Settings.is_night_mode);
            return true;
        } else if (preference == mExitBeSurePreference) {
            //           LogUtils.d(CommonInfo.TAG, "mExitBeSurePreference " + mExitBeSurePreference.isChecked());
            Settings.is_back_by_twice = Boolean.parseBoolean(newValue.toString());
            mSettings.putBoolean(Settings.BACK_BY_TWICE_KEY, Settings.is_back_by_twice);
            return true;
        } else if (preference == mNoPicModePreference) {
//            LogUtils.d(CommonInfo.TAG, "mNoPicModePreference " + mNoPicModePreference.isChecked());
            Settings.is_no_picture_mode = Boolean.parseBoolean(newValue.toString());
            mSettings.putBoolean(Settings.NO_PICTURE_KEY, Settings.is_no_picture_mode);
            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == mLanguagePreference) {
            LogUtils.d(CommonInfo.TAG, "mLanguagePreference " + mLanguagePreference.getValue());
        } else if (preference == mFontPreference) {
            LogUtils.d(CommonInfo.TAG, "mFontPreference " + mFontPreference.getValue());
        } else if (preference == mNightModePreference) {
            LogUtils.d(CommonInfo.TAG, "mNightModePreference " + mNightModePreference.isChecked());
        } else if (preference == mExitBeSurePreference) {
            LogUtils.d(CommonInfo.TAG, "mExitBeSurePreference " + mExitBeSurePreference.isChecked());
        } else if (preference == mNoPicModePreference) {
            LogUtils.d(CommonInfo.TAG, "mNoPicModePreference " + mNoPicModePreference.isChecked());
        } else if (preference == mClearPicCachePreference) {
            LogUtils.d(CommonInfo.TAG, "mClearPicCachePreference " + mClearPicCachePreference.isSelectable());
        } else if (preference == mClearAllCachePreference) {
            LogUtils.d(CommonInfo.TAG, "mClearAllCachePreference " + mClearAllCachePreference.isSelectable());
        }
        return false;
    }
}
