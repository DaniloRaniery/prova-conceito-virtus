import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { AuthorAcess } from './author-acess.model';
import { AuthorAcessPopupService } from './author-acess-popup.service';
import { AuthorAcessService } from './author-acess.service';

@Component({
    selector: 'jhi-author-acess-delete-dialog',
    templateUrl: './author-acess-delete-dialog.component.html'
})
export class AuthorAcessDeleteDialogComponent {

    authorAcess: AuthorAcess;

    constructor(
        private authorAcessService: AuthorAcessService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.authorAcessService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'authorAcessListModification',
                content: 'Deleted an authorAcess'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-author-acess-delete-popup',
    template: ''
})
export class AuthorAcessDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private authorAcessPopupService: AuthorAcessPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.authorAcessPopupService
                .open(AuthorAcessDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
