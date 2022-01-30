(function() {
    const tableE = document.getElementById('table-reservations')
    const searchDateSubmit = document.getElementById('date-search-submit')
    const searchDateStart = document.getElementById('date-start')
    const searchDateEnd = document.getElementById('date-end')
    const pagingE = document.getElementById('pages-reservations')
    const searchE = document.getElementById('search-form-reservations')

    const isAuthAdmin = tableE.getAttribute('data-auth-admin') ?? false

    const detailAction = (elem, id) => {
        elem.addEventListener('click', (e)=>{
            if(e.shiftKey){
                e.preventDefault()
                reservationTable.setParams({page: 1, author : id})
                reservationTable.load(true)
            }
        })
        if (isAuthAdmin)
        {
            const detail = new UserDetail(elem, id)
            detail.detailAction()
        }
    }


    const reservationTableBuilder =
        new DataTableBuilder(tableE,'/api/reservations', '/reservation/')
            .addField('id', 'link', 'Id', (elem, id) => {elem.setAttribute('href', '/reservation/' + id) })
            .addActionHeaderField('author', 'link', 'Author',
                (elem) => elem.author.name,
                (elem) => elem.author.id,
                detailAction,
                (elem) => {
                    elem.addEventListener('click', (e)=>{
                        if(e.shiftKey) {
                            delete reservationTable.params['author']
                            reservationTable.setParams({page: 1})
                            reservationTable.load(true)
                        }
                    })
                }
            )
            .addActionHeaderField('room', 'link', 'Room',
                (elem) => elem.room.name,
                (elem) => elem.room.id,
                (elem, id, val) => {
                    elem.setAttribute('href', `/room/${id}`)
                    elem.addEventListener('click', (e)=>{
                        if(e.shiftKey){
                            e.preventDefault()
                            reservationTable.setParams({page: 1, room : val["room"]["id"]})
                            reservationTable.load(true)
                        }
                    })
                },
                (elem) => {
                    elem.addEventListener('click', (e)=>{
                        if(e.shiftKey) {
                            delete reservationTable.params['room']
                            reservationTable.setParams({page: 1})
                            reservationTable.load(true)
                        }
                    })
                }
            )
            .addField('status', 'text', 'Status')
            .addField('start', 'dateTime', 'Start')
            .addField('until', 'dateTime', 'Until')


    const reservationTable = reservationTableBuilder.build()

    reservationTable.subscribe(new TablePaging(reservationTable, pagingE))

    const search1 = new DateSearch(reservationTable, searchDateSubmit, searchDateStart, searchDateEnd)
    reservationTable.subscribe(search1)

    // const search2 = new TableSearch(reservationTable, searchE, ['author'])
    // reservationTable.subscribe(search2)

    //todo: initial state for user detail - addeventlisteners on click
    reservationTable.content.innerHTML = ''
    reservationTable.load(true)
})()