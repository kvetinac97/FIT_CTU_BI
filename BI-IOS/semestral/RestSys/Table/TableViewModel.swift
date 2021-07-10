//
//  TableViewModel.swift
//  RestSys
//
//  Created by Thats Me on 20.01.2021.
//

import Foundation

final class TableViewModel: ViewModelRead<Table> {
    
    func loadTables () {
        super.loadEntities(url: "/tables/")
    }
    
    func removeTable (table: Table) {
        networkService.delete(url: "/tables/" + String(table.id)) { [weak self] result in
            switch result {
                case let .failure(error):
                    self?.displayError(error)
                case .success(_):
                    if let index = self?.entities.firstIndex( where: { (t) -> Bool in return t.id == table.id } ) {
                        DispatchQueue.main.async { [weak self] in
                            self?.entities.remove(at: index)
                        }
                    }
            }
        }
    }
    
}
