(function() {
    const tableE = document.getElementById('table-people')
    const searchE = document.getElementById('search-form-people')
    const pagingE = document.getElementById('pages-people')

    const userTable =
        new DataTableBuilder(tableE,'/api/users', '/user')
            .addField('id', 'text', 'Id')
            .addField('name', 'link', 'Name', (elem, id) => {elem.setAttribute('href', '/user/' + id) })
            .addField('email', 'text', 'Email')
            .addField('tel', 'text', 'Phone number')
            .build()

    userTable.subscribe(new TablePaging(userTable, pagingE))
    const search = new TableSearch(userTable, searchE, ['name'])
    userTable.subscribe(search)
})()