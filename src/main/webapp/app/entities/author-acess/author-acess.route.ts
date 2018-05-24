import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { AuthorAcessComponent } from './author-acess.component';
import { AuthorAcessDetailComponent } from './author-acess-detail.component';
import { AuthorAcessPopupComponent } from './author-acess-dialog.component';
import { AuthorAcessDeletePopupComponent } from './author-acess-delete-dialog.component';

export const authorAcessRoute: Routes = [
    {
        path: 'author-acess',
        component: AuthorAcessComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jHipsterBlogApp.authorAcess.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'author-acess/:id',
        component: AuthorAcessDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jHipsterBlogApp.authorAcess.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const authorAcessPopupRoute: Routes = [
    {
        path: 'author-acess-new',
        component: AuthorAcessPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jHipsterBlogApp.authorAcess.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'author-acess/:id/edit',
        component: AuthorAcessPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jHipsterBlogApp.authorAcess.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'author-acess/:id/delete',
        component: AuthorAcessDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jHipsterBlogApp.authorAcess.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
