/* Copyright (c) 2009 Matthias Käppler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.droidfu.activities;

import android.content.Intent;

public interface BetterActivity {

    public int getWindowFeatures();

    /**
     * @return true, if the activity is recovering from in interruption (i.e.
     *         onRestoreInstanceState was called.
     */
    public boolean isRestoring();

    /**
     * @return true, if the activity is "soft-resuming", i.e. onResume has been
     *         called without a prior call to onCreate
     */
    public boolean isResuming();

    /**
     * @return true, if the activity is launching, i.e. is going through
     *         onCreate but is not restoring.
     */
    public boolean isLaunching();

    /**
     * Android doesn't distinguish between your Activity being paused by another
     * Activity of your own application, or by an Activity of an entirely
     * different application. This function only returns true, if your Activity
     * is being paused by an Activity of another app, thus hiding yours.
     * 
     * @return true, if the Activity is being paused because an Activity of
     *         another application received focus.
     */
    public boolean isApplicationBroughtToBackground();

    /**
     * Retrieves the current intent that was used to create or resume this
     * activity. If the activity received a call to onNewIntent (e.g. because it
     * was launched in singleTop mode), then the Intent passed to that method is
     * returned. Otherwise the returned Intent is the intent returned by
     * getIntent (which is the Intent which was used to initially launch this
     * activity).
     * 
     * @return the current {@link Intent}
     */
    public Intent getCurrentIntent();

    public boolean isLandscapeMode();

    public boolean isPortraitMode();

}
