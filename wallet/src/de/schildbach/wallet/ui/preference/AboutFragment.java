/*
 * Copyright 2014-2015 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.schildbach.wallet.ui.preference;

import org.bitcoinj.core.VersionMessage;

import de.schildbach.wallet.Constants;
import de.schildbach.wallet.WalletApplication;
import de.schildbach.wallet_test.BuildConfig;
import de.schildbach.wallet_test.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Andreas Schildbach
 */
public final class AboutFragment extends PreferenceFragment {
    private Activity activity;
    private WalletApplication application;
    private PackageManager packageManager;

    private static final String KEY_ABOUT_VERSION = "about_version";
    private static final String KEY_ABOUT_MARKET_APP = "about_market_app";
    private static final String KEY_ABOUT_CREDITS_BITCOINJ = "about_credits_bitcoinj";

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);

        this.activity = activity;
        this.application = (WalletApplication) activity.getApplication();
        this.packageManager = activity.getPackageManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        v.setBackgroundDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.black_background));

        return v;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference_about);

        findPreference(KEY_ABOUT_VERSION)
                .setSummary(application.packageInfo().versionName + (BuildConfig.DEBUG ? " (debuggable)" : ""));
        Intent marketIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(String.format(Constants.MARKET_APP_URL, activity.getPackageName())));
        if (packageManager.resolveActivity(marketIntent, 0) == null)
            marketIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(String.format(Constants.WEBMARKET_APP_URL, activity.getPackageName())));
        findPreference(KEY_ABOUT_MARKET_APP).setIntent(marketIntent);

        String title = getString(R.string.about_credits_bitcoinj_title, VersionMessage.BITCOINJ_VERSION);
        SpannableString titleColor= new SpannableString(title);
        titleColor.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.vivid_red)), 0, title.length(), 0);
        findPreference(KEY_ABOUT_CREDITS_BITCOINJ).setTitle(titleColor);
    }
}
