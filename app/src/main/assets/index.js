
log("芜湖")
launch('jp.co.cygames.umamusume')
waitFor(inMainTitle)

tap(497,914)
tap(257,653)
tap(387,624)
tap(448,359)

tap(236,413)
text("1231231")
tap(259,548)
tap(259,548)
text("1231231")


function inMainTitle() {
    const img = screen.capture()
    if( img.getPixel(499,903) === "#FFFFFF" && img.getPixel(497,914) === "#B57039") {
        return true
    }
    return false
}