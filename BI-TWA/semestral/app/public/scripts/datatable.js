class DataTableContentFactory {
    constructor(types, defaults, actions, transformations) {
        this.types = types
        this.actions = actions
        this.transformations = transformations
        this.defaults = defaults
    }

    create(key, val, id, row){
        const li = document.createElement('li')
        li.classList.add(key)

        const transformation = this.transformations[key]
        if (transformation) val = transformation(val)

        // console.log(key, this.types[key], val, id)
        switch (this.types[key]){
            case 'link':
                li.textContent = ''
                const link = document.createElement('a')
                const action = this.actions[key]
                li.append(link)

                if (action){ action(link, id, row); }

                link.textContent = val ?? '\u2192'
                break
            case 'button':
                li.textContent = ''
                const button = document.createElement('button')
                const btnAction = this.actions[key]
                if (btnAction){ btnAction(button, id, row); }

                li.append(button)
                button.textContent = val ?? '\u2192'
                break
            case 'image':
                li.textContent = ''
                const img = document.createElement('img')
                img.setAttribute('src', '/uploads/' + val)
                li.append(img)
                break
            case 'list':
                li.textContent = ''
                const roles = document.createElement('ul')
                for(const vals of val){
                    const role = document.createElement('li');
                    role.textContent = vals["title"]
                    roles.append(role);
                }
                li.append(roles)
                break
            case 'dateTime':
                let date = ""
                if (val){
                    const d = val.replace('T', ' ').replace('+', ' ').split(' ')
                    date = `${d[0]} ${d[1]}`
                }else{
                    date = this.defaults[key]
                }
                li.textContent = date
                break
            case 'bool':
                li.textContent = val ? 'Yes' : 'No'
                break
            default:
                if (val === true) val = 'Yes'
                if (val === false) val = 'No'
                li.textContent = val ?? this.defaults[key]
        }

        return li;
    }
}

class DataTableBuilder{
    constructor(tableE, apiEndpoint, stateUrl){
        // DATA
        this.apiEndpoint = apiEndpoint
        this.stateUrl = stateUrl
        this.actions = {}
        this.headerActions = {}
        this.transformations = {}
        this.getters = {}
        this.idGetters = {}

        // TABLE
        this.tableE = tableE
        this.fields = []
        this.types = {}
        this.titles = {}
        this.orderables = []
        this.defaults = {}
    }

    addField(field, type, title, action, transformation){
        if (field){
            this.fields.push(field)
            if (action){
                this.actions[field] = action
            }
            if (transformation) {
                this.transformations[field] = transformation
            }
            this.types[field] = type ?? "text"
            this.titles[field] = title ?? field
        }
        return this
    }

    addGetterField(field, type, title, getter){
        if (field){
            this.fields.push(field)

            this.getters[field] = getter
            this.types[field] = type ?? "text"
            this.titles[field] = title ?? field
        }
        return this
    }

    addActionField(field, type, title, valueGetter, idGetter, action){
        if (field){
            this.fields.push(field)
            if (action){
                this.actions[field] = action
            }
            this.getters[field] = valueGetter
            this.idGetters[field] = idGetter
            this.types[field] = type ?? "text"
            this.titles[field] = title ?? field
        }
        return this
    }

    addActionHeaderField(field, type, title, valueGetter, idGetter, action, headerClickAction){
        this.addActionField(field, type, title, valueGetter, idGetter, action)
        this.headerActions[field] = headerClickAction
        return this
    }

    addOrderableField(field, type, title, action){
        this.addField(field, type, title, action)
        this.orderables.push(field)
        return this
    }

    addFieldWithDefault(field, type, title, defaultVal){
        this.addField(field, type, title)
        this.defaults[field] = defaultVal
        return this
    }

    build(){
        return new DataTable(
            this.tableE,
            this.apiEndpoint,
            this.stateUrl,
            this.fields,
            this.types,
            this.titles,
            this.orderables,
            this.defaults,
            this.actions,
            this.headerActions,
            this.transformations,
            this.getters,
            this.idGetters)
    }
}

class TableSubscriber{
    constructor(table) {
        this.table = table
    }

    update() {}
}

class TablePaging extends TableSubscriber{
    constructor(table, pagingE) {
        super(table);
        this.pagingE = pagingE
        this.pages = pagingE.querySelectorAll('li a')
        this.init()
    }

    init(){
        for(const page of this.pages){
            page.addEventListener('click', (e) => {
                e.preventDefault()
                this.table.setParams({'page' : page.getAttribute('data-page') ?? 1})
                this.table.load(true)
            });
        }
    }

    update(data){
        this.pagingE.innerHTML = ''
        const wrapper = document.createDocumentFragment()

        for (let i = 1; i <= data.pages; ++i){
            const li = document.createElement('li')
            const a = document.createElement('a')
            a.setAttribute('href', '#')
            a.textContent = i.toString()
            a.addEventListener('click', (e) => {
                this.table.setParams({'page': i})
                e.preventDefault();
                this.table.load(true)
            });

            if (i === data.page){
                a.classList.add('active-page')
            }
            li.append(a)
            wrapper.append(li)
        }

        this.pagingE.append(wrapper)
    }
}

class TableSearch extends TableSubscriber{
    constructor(table, formE, formFields) {
        super(table);
        this.formE = formE
        this.formFields = formFields
        this.init()
    }

    init(){
        this.formE.addEventListener('submit', (e) => {
            e.preventDefault()
            const params = {}
            this.formFields.forEach(key => {
                let element = this.formE.elements[key]
                if (element.type !== "submit") {
                    params[key] = element.value
                }
                console.log(element.value)
                console.log(key)
            });
            this.table.setParams(params)
            this.table.resetPage()
            // const name = document.getElementById('search-input').value
            // const role = document.getElementById('role').value
            this.table.load(true)
            // this.search(name, role)
        })
    }

    update(data){
        //TODO
    }
}

class DateSearch extends TableSubscriber{

    static months = ['leden', 'únor', 'březen', 'duben', 'květen', 'červen', 'červenec','srpen', 'záři', 'říjen', 'listopad', 'prosinec']

    startChosen = false
    endChosen   = false
    startDate   = new Date()
    endDate     = new Date()

    constructor(table, submitE, startE, endE) {
        super(table);
        this.submitE = submitE
        this.startE  = startE
        this.endE    = endE
        this.init()
    }

    init(){
        
        this.submitE.addEventListener('click', (e) => {
            e.preventDefault();
            const params = {};
            if (this.startChosen)
                params['from'] = this.startDate.toISOString().substring(0, 10);
            if (this.endChosen)
                params['until'] = this.endDate.toISOString().substring(0, 10);
            this.table.setParams(params);
            this.table.resetPage();
            this.table.load(true);
            toggleHidden(['toggle-date-start','toggle-date-end'],['date-start','date-end','date-search-submit'])
        });

        this.startE.addEventListener('click', (e) => {
            if ( 
                !(e.target.firstChild.nodeValue >= 1 && e.target.firstChild.nodeValue <= 31 )
            )
                return;
            e.preventDefault()
            let day = parseInt(e.target.firstChild.nodeValue) + 1;
            let monthstr = this.startE.querySelector('#month').firstChild.nodeValue;
            let month;
            DateSearch.months.forEach((m,i) => {
                if ( m == monthstr )
                    month = i;
            });
            let year = this.startE.querySelector('#year').firstChild.nodeValue;
            if ( e.target.classList[0] == 'diff-month' ){
                if ( day >= 21 ){
                    month -= 1;
                    if ( month == -1 ){
                        year -= 1;
                        month = 11;
                    }
                }
                else if ( day <= 7 ){
                    month += 1;
                    if ( month == 12 ){
                        year += 1;
                        month = 0;
                    }
                }
            }
            this.startDate = new Date( year, month, day );
            this.startChosen = true;
        });


        this.endE.addEventListener('click', (e) => {
            if ( 
                !(e.target.firstChild.nodeValue >= 1 && e.target.firstChild.nodeValue <= 31 )
            )
                return;
            e.preventDefault()
            let day = parseInt(e.target.firstChild.nodeValue) + 1;
            let monthstr =  this.endE.querySelector('#month').firstChild.nodeValue;
            let month;
            DateSearch.months.forEach((m,i) => {
                if ( m == monthstr )
                    month = i;
            });
            let year = this.endE.querySelector('#year').firstChild.nodeValue;
            if ( e.target.classList[0] == 'diff-month' ){
                if ( day >= 21 ){
                    month -= 1;
                    if ( month == -1 ){
                        year -= 1;
                        month = 11;
                    }
                }
                else if ( day <= 7 ){
                    month += 1;
                    if ( month == 12 ){
                        year += 1;
                        month = 0;
                    }
                }
            }
            this.endDate = new Date( year, month, day );
            this.endChosen = true;
        });
    }
}

class Loader{
    constructor(tableE) {
        tableE.classList.add('loader-wrapper')
        const loaderE = document.createElement('div')
        loaderE.classList.add('loader')
        loaderE.classList.add('loader-hidden')
        const loaderContent = document.createElement('div')
        loaderContent.classList.add('loader-content')
        loaderContent.textContent = "Loading..."
        loaderE.append(loaderContent)
        tableE.append(loaderE)
        this.elem = loaderE
    }

    start(){
        this.elem.classList.remove('loader-hidden')
    }

    stop(){
        this.elem.classList.add('loader-hidden')
    }

    toggle(){
        this.elem.classList.toggle('loader-hidden')
    }

}

class DataTable{
    constructor(tableE,
                apiEndpoint,
                stateUrl,
                fields = [],
                types = {},
                titles = {},
                orderables = [],
                defaults = {},
                actions = {},
                headerActions = {},
                transformations = {},
                valueGetters = {},
                idGetters = {})
    {
        // COMPONENTS
        this.tableE = tableE
        this.content = tableE.querySelector('.table-content')
        this.header = tableE.querySelector('.table-header')

        const loaderWrapperE = tableE.parentElement
        if (loaderWrapperE){ this.loader = new Loader(loaderWrapperE) }

        // ADD EVENT LISTENERS
        window.addEventListener('popstate', (event) => {
            console.log("POPSTATE")
            if (!event.state){
                history.back();
                return
            }

            const state = event.state.data
            console.log(state)
            this.data = state["data"]
            this.totalPages = state["pages"]
            this.currentPage = state["page"]
            console.log(this.data)

            this.updateHeader()
            this.subscribers.forEach((subscriber) => {
                subscriber.update(state)
            })
            this.update()
        });


        // FIELDS
        this.contentFactory = new DataTableContentFactory(types, defaults, actions, transformations)
        this.fields = fields
        this.titles = titles
        this.orderables = orderables
        this.valueGetters = valueGetters
        this.idGetters = idGetters
        this.headerActions = headerActions

        // DATA
        this.apiEndpoint = apiEndpoint
        this.stateUrl = stateUrl
        this.data = []
        this.currentPage = null
        this.totalPages = null
        this.params = {}
        for(const entry of new URLSearchParams(window.location.search).entries()){
            this.params[entry[0]] = entry[1]
        }

        this.subscribers = []
        this.updateHeader()
    }

    load(remember){
        const testUrl = new URL(this.apiEndpoint, window.location)
        const stateUrl =  new URL(this.stateUrl, window.location)
        // console.log(stateUrl)

        Object.keys(this.params).forEach((k)=>{
            testUrl.searchParams.set(k, this.params[k])
            stateUrl.searchParams.set(k, this.params[k])
        })

        this.loader.start()
        // Fetch data
        fetch(testUrl.toString())
            .then(response => response.json())
            .then(data => {
                console.log(data)
                this.data = data["data"]
                this.totalPages = data["pages"]
                this.currentPage = data["page"]
                // Update url
                if (remember === true){
                    window.history.pushState({"data": data},"", stateUrl.toString());
                }

                this.subscribers.forEach((subscriber) => {
                    subscriber.update(data)
                })

                this.update()
                this.loader.stop()
            })
            .catch(err => {
                console.log(err)
                this.loader.stop()
            })
    }

    update(){
        const wrapper = document.createDocumentFragment()

        for (let i = 0; i < this.data.length; i++) {
            const row = document.createElement('article')
            row.classList.add('table-row')

            const ol = document.createElement('ol')
            const item = this.data[i]

            this.fields.forEach((field) => {
                const valGetter = this.valueGetters[field]
                const val = valGetter ? valGetter(item) : item[field]

                const idGetter = this.idGetters[field] ?? this.valueGetters["id"]
                const id = idGetter ? idGetter(item) : item["id"]

                const li = this.contentFactory.create(field, val, id, item);
                ol.appendChild(li)
            })

            row.append(ol)
            wrapper.appendChild(row)
        }

        this.content.innerHTML = ''
        this.content.appendChild(wrapper)
    }

    updateHeader(){
        const row = document.createElement('ol')

        for (const field of this.fields) {
            const li = document.createElement('li')

            const headerAction = this.headerActions[field]
            if (headerAction){ headerAction(li) }

            li.classList.add(field)

            li.textContent = this.titles[field] ?? field
            if (this.orderables.includes(field)){
                li.textContent = ""
                const a = document.createElement('a');
                a.setAttribute('href', window.location)
                a.addEventListener('click', (e) => {
                    e.preventDefault()
                    let dir = "ASC"
                    if (this.params['orderBy'] === field){
                        if (this.params['orderDir']){
                            dir = (this.params['orderDir'] === "ASC") ? "DESC" : "ASC";
                        }
                    }
                    else{
                        this.params['orderBy'] = field
                    }
                    this.params['orderDir'] = dir

                    this.resetPage()
                    this.load(true)
                })
                a.textContent = this.titles[field] ?? field
                li.append(a)
            }
            row.appendChild(li)
        }

        this.header.innerHTML = ''
        this.header.appendChild(row)
    }


    setParams(params){
        Object.keys(params).forEach((key) => {
            console.log(key)
            this.params[key] = params[key]
            console.log(params)
            console.log(this.params)
        })
    }

    resetPage() {
        this.params['page'] = 1
    }


    subscribe(subscriber){
        this.subscribers.push(subscriber)
    }
}
