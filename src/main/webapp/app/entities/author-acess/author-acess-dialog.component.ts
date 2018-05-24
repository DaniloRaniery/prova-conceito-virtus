import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { AuthorAcess } from './author-acess.model';
import { AuthorAcessPopupService } from './author-acess-popup.service';
import { AuthorAcessService } from './author-acess.service';

@Component({
    selector: 'jhi-author-acess-dialog',
    templateUrl: './author-acess-dialog.component.html'
})
export class AuthorAcessDialogComponent implements OnInit {

    authorAcess: AuthorAcess;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private authorAcessService: AuthorAcessService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.authorAcess.id !== undefined) {
            this.subscribeToSaveResponse(
                this.authorAcessService.update(this.authorAcess));
        } else {
            this.subscribeToSaveResponse(
                this.authorAcessService.create(this.authorAcess));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<AuthorAcess>>) {
        result.subscribe((res: HttpResponse<AuthorAcess>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: AuthorAcess) {
        this.eventManager.broadcast({ name: 'authorAcessListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-author-acess-popup',
    template: ''
})
export class AuthorAcessPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private authorAcessPopupService: AuthorAcessPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.authorAcessPopupService
                    .open(AuthorAcessDialogComponent as Component, params['id']);
            } else {
                this.authorAcessPopupService
                    .open(AuthorAcessDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
