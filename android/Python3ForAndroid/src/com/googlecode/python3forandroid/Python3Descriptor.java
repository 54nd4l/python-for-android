/*
 * Copyright (C) 2015 Shimoda <kuri65536@hotmail.com>
 * Copyright (C) 2010-2011 Naranjo Manuel Francisco <manuel@aircable.net>
 * Copyright (C) 2010-2011 Robbie Matthews <rjmatthews62@gmail.com>
 * Copyright (C) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.googlecode.python3forandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.googlecode.android_scripting.FileUtils;
import com.googlecode.android_scripting.Log;
import com.googlecode.android_scripting.pythoncommon.PythonConstants;
import com.googlecode.android_scripting.pythoncommon.PythonDescriptor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Python3Descriptor extends PythonDescriptor {

  private static final String PYTHON_BIN = "python3/bin/python3";
  private static final int LATEST_VERSION = 5;
  private int cache_version = -1;
  private int cache_extras_version = -1;
  private int cache_scripts_version = -1;
  private SharedPreferences mPreferences;

    @Override
    protected String pathBin() {
        return "/bin/python";
    }

    @Override
    protected String pathShlib() {
        return "/lib";
    }

    @Override
    protected String pathEgg() {
        return this.pathShlib() + "/python3.4/egg-info";
    }

    @Override
    protected String pathSitepkgs() {
        return this.pathShlib() + "/python3.4/site-packages";
    }

    @Override
    protected String pathDynload() {
        return this.pathShlib() + "/python3.4/lib-dynload";
    }

  @Override
  public String getBaseInstallUrl() {
    return Python3Urls.URL_BIN;
  }

  @Override
  protected String urlSrc() {
    return Python3Urls.URL_SRC;
  };

  @Override
  protected String pathVersion() {
    return "python3-alpha/LATEST_VERSION";
  };

  @Override
  public String getExtension() {
        return ".py3";
  }

  @Override
  public String getName() {
    return "python3";
  }

  @Override
  public String getNiceName() {
    return "Python 3.4.3";
  }

  @Override
  public File getBinary(Context context) {
    File f = new File(context.getFilesDir(), PYTHON_BIN);
    return f;
    // return new File(getExtrasPath(context), PYTHON_BIN);
  }

  @Override
  public Map<String, String> getEnvironmentVariables(Context context) {
    Map<String, String> values = new HashMap<String, String>();
        String home = getHome(context);
    String pylibs = getExtras();

    // BUG: current binary does not recognize PYTHONHOME collectly... why...?
    // values.put(ENV_HOME, libs + ":" + new File(home, "python3").getAbsolutePath());
        values.put(PythonConstants.ENV_LD, home + pathShlib());
    values.put(PythonConstants.ENV_PATH,
               pylibs + ":" +
               home + this.pathShlib() + ":" +
               home + this.pathSitepkgs() + ":" +
               home + this.pathDynload());
    values.put(PythonConstants.ENV_EGGS, home + this.pathEgg());
    values.put(PythonConstants.ENV_EXTRAS, getExtrasRoot());
    String temp = context.getCacheDir().getAbsolutePath();
    values.put(PythonConstants.ENV_TEMP, temp);
    try {
      FileUtils.chmod(context.getCacheDir(), 0777); // Make sure this is writable.
    } catch (Exception e) {
    }
    values.put("HOME", Environment.getExternalStorageDirectory().getAbsolutePath());
    for (String k : values.keySet()) {
      Log.d(k + " : " + values.get(k));
    }
    return values;
  }
}
