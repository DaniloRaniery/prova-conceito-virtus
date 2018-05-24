import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { AuthorAcess } from './author-acess.model';
import { AuthorAcessService } from './author-acess.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-author-acess',
    templateUrl: './author-acess.component.html'
})
export class AuthorAcessComponent implements OnInit, OnDestroy {
authorAcesses: AuthorAcess[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private authorAcessService: AuthorAcessService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.authorAcessService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: HttpResponse<AuthorAcess[]>) => this.authorAcesses = res.body,
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
       }
        this.authorAcessService.query().subscribe(
            (res: HttpResponse<AuthorAcess[]>) => {
                this.authorAcesses = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInAuthorAcesses();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: AuthorAcess) {
        return item.id;
    }
    registerChangeInAuthorAcesses() {
        this.eventSubscriber = this.eventManager.subscribe('authorAcessListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
