/*
 * Copyright (c) 2005, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.script.rhino;

import org.mozilla.javascript.ClassShutter;

/**
 * This class prevents script access to certain sensitive classes.
 * Note that this class checks over and above SecurityManager. i.e., although
 * a SecurityManager would pass, class shutter may still prevent access.
 *
 * @author A. Sundararajan
 * @since 1.6
 */
final class RhinoClassShutter implements ClassShutter {
    private static final String[] protectedClasses = new String[]{
            "dalvik.system",
            "java.lang.Class",
            "java.lang.ClassLoader",
            "java.net.URLClassLoader",
            "cn.hutool.core.lang.JarClassLoader",
            "org.mozilla.javascript.DefiningClassLoader",
            "java.lang.Runtime",
            "java.io.File",
            "java.security.AccessController",
            "java.nio.file.Paths",
            "java.nio.file.Files",
            "io.legado.app.data.AppDatabase",
            "io.legado.app.data.AppDatabaseKt",
            "io.legado.app.utils.ContextExtensionsKt",
            "android.content.Intent",
            "androidx.core.content.FileProvider",
            "android.provider.Settings",
            "androidx.sqlite.db",
            "splitties.init.AppCtxKt",
            "android.app.ActivityThread",
            "android.app.AppGlobals"
    };

    private static RhinoClassShutter sInstance;

    private RhinoClassShutter() {
    }

    static synchronized ClassShutter getInstance() {
        if (sInstance == null) {
            sInstance = new RhinoClassShutter();
        }
        return sInstance;
    }

    @Override
    public boolean visibleToScripts(String fullClassName) {
        if (!containsProtectedClass(fullClassName)) {
            var className = fullClassName;
            while (className.contains(".")) {
                className = substringBeforeLast(className,".");
                if (containsProtectedClass(className)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static boolean containsProtectedClass(String fullClassName) {
        for (String className : protectedClasses) {
            if (className.equals(fullClassName)) return true;
        }
        return false;
    }

    private static String substringBeforeLast(String string, String delimiter) {
        int index = string.lastIndexOf(delimiter);
        return (index == -1) ? string : string.substring(0, index);
    }

}
