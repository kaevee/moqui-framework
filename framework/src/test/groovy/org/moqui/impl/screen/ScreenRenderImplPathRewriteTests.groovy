/*
 * This software is in the public domain under CC0 1.0 Universal plus a
 * Grant of Patent License.
 *
 * To the extent possible under law, the author(s) have dedicated all
 * copyright and related and neighboring rights to this software to the
 * public domain worldwide. This software is distributed without any
 * warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication
 * along with this software (see the LICENSE.md file). If not, see
 * <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.moqui.impl.screen

import org.moqui.Moqui
import org.moqui.context.ExecutionContext
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class ScreenRenderImplPathRewriteTests extends Specification {
    private static final String ROOT_SCREEN_LOCATION = "component://webroot/screen/webroot.xml"

    @Shared
    ExecutionContext ec
    @Shared
    ScreenRenderImpl sri

    def setupSpec() {
        ec = Moqui.getExecutionContext()
        ec.user.loginUser("john.doe", "moqui")

        sri = (ScreenRenderImpl) ec.screen.makeRender()
        sri.rootScreen(ROOT_SCREEN_LOCATION)
        sri.rootScreenDef = sri.sfi.getScreenDefinition(ROOT_SCREEN_LOCATION)
    }

    def cleanupSpec() {
        ec.destroy()
    }

    @Unroll
    def "rewrite menu path from #screenMountedPath to #qvt2Path for #screenPath"() {
        when:
        List<Map> menuDataList = sri.getMenuData(pathList(screenPath), qvt2Path, screenMountedPath, true)
        Map lastMap = menuDataList.last()

        then:
        menuDataList
        lastMap.path == expectedPath
        lastMap.pathWithParams == expectedPathWithParams

        where:
        screenPath               | qvt2Path   | screenMountedPath | expectedPath               | expectedPathWithParams
        "/apps"                  | "/qapps2"  | "/apps"           | "/qapps2"                  | "/qapps2"
        "/apps/tools/dashboard"  | "/qapps2/" | " /apps/ "        | "/qapps2/tools/dashboard"  | "/qapps2/tools/dashboard"
        "/apps/tools/dashboard"  | "/qapps2"  | "/app"            | "/apps/tools/dashboard"    | "/apps/tools/dashboard"
    }

    private static ArrayList<String> pathList(String path) {
        return new ArrayList<>(path.split('/').findAll { it != null && it.length() > 0 } as List<String>)
    }
}
