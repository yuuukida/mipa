function tapUntil() {
    const args = []
    for(const i of arguments){
        args.push(i)
    }
    foo = args.shift()
    while(!foo()) {
        tap.apply(this,args)
    }
}

function tapAfter() {
    const args = []
    for(const i of arguments){
        args.push(i)
    }
    foo = args.shift()
    while(!foo()) {
        sleep(1000)
    }
    tap.apply(this,args)
}


function swipeUntil() {
    const args = []
    for(const i of arguments){
        args.push(i)
    }
    foo = args.shift()
    while(!foo()) {
        swipe.apply(this,args)
    }
}

function swipeAfter() {
    const args = []
    for(const i of arguments){
        args.push(i)
    }
    foo = args.shift()
    while(!foo()) {
        sleep(1000)
    }
    swipe.apply(this,args)
}