import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { AuthorAcess } from './author-acess.model';
import { AuthorAcessService } from './author-acess.service';

@Component({
    selector: 'jhi-author-acess-detail',
    templateUrl: './author-acess-detail.component.html'
})
export class AuthorAcessDetailComponent implements OnInit, OnDestroy {

    authorAcess: AuthorAcess;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private authorAcessService: AuthorAcessService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAuthorAcesses();
    }

    load(id) {
        this.authorAcessService.find(id)
            .subscribe((authorAcessResponse: HttpResponse<AuthorAcess>) => {
                this.authorAcess = authorAcessResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAuthorAcesses() {
        this.eventSubscriber = this.eventManager.subscribe(
            'authorAcessListModification',
            (response) => this.load(this.authorAcess.id)
        );
    }
}
