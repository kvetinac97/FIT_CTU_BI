(function() {
    const tableE = document.getElementById('table-groups')
    const searchE = document.getElementById('search-form-groups')
    const pagingE = document.getElementById('pages-groups')

    const isAuthAdmin = tableE.getAttribute('data-auth-admin') ?? false

    const detailAction = (elem, id) => {
        const detail = new UserDetail(elem, id)
        detail.detailAction()
    }

    const groupTableBuilder =
        new DataTableBuilder(tableE,'/api/groups?show-manager=true', '/group')
            .addGetterField('id', 'text', 'Id', (val)=>val["group"]["id"])
            .addActionField('name', 'link', 'Name',
                (val)=>val["group"]["name"],
                null,
                (elem, id) => {elem.setAttribute('href', '/group/' + id)})


    if(isAuthAdmin){
        groupTableBuilder
            .addActionField('manager', 'link', 'Manager',
                (val) =>
                {
                    const manager = val.manager;
                    return (manager === null ? "No Manager" : manager.user.name)
                },
                (val) =>
                {
                    const manager = val.manager;
                    return (manager === null ? null : manager.user.id)
                },
                detailAction
            )
    }

    const groupTable = groupTableBuilder.build()


    groupTable.subscribe(new TablePaging(groupTable, pagingE))
    const search = new TableSearch(groupTable, searchE, ['name'])
    groupTable.subscribe(search)

    //todo: initial state for user detail - addeventlisteners on click
    groupTable.content.innerHTML = ''
    groupTable.load()
})()