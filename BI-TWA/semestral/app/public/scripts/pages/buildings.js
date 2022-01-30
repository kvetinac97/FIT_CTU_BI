(function() {
    const tableE = document.getElementById('table-building')
    const searchE = document.getElementById('search-form-building')
    const pagingE = document.getElementById('pages-building')

    const buildingTable =
        new DataTableBuilder(tableE,'/api/buildings', '/building')
            .addField('id', 'text', 'Id')
            .addField('name', 'link', 'Name', (elem, id) => {elem.setAttribute('href', '/building/' + id) })
            .build()

    buildingTable.subscribe(new TablePaging(buildingTable, pagingE))
    const search = new TableSearch(buildingTable, searchE, ['name'])
    buildingTable.subscribe(search)
})()