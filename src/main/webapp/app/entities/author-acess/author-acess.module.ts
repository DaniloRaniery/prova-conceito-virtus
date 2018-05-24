import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JHipsterBlogSharedModule } from '../../shared';
import {
    AuthorAcessService,
    AuthorAcessPopupService,
    AuthorAcessComponent,
    AuthorAcessDetailComponent,
    AuthorAcessDialogComponent,
    AuthorAcessPopupComponent,
    AuthorAcessDeletePopupComponent,
    AuthorAcessDeleteDialogComponent,
    authorAcessRoute,
    authorAcessPopupRoute,
} from './';

const ENTITY_STATES = [
    ...authorAcessRoute,
    ...authorAcessPopupRoute,
];

@NgModule({
    imports: [
        JHipsterBlogSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        AuthorAcessComponent,
        AuthorAcessDetailComponent,
        AuthorAcessDialogComponent,
        AuthorAcessDeleteDialogComponent,
        AuthorAcessPopupComponent,
        AuthorAcessDeletePopupComponent,
    ],
    entryComponents: [
        AuthorAcessComponent,
        AuthorAcessDialogComponent,
        AuthorAcessPopupComponent,
        AuthorAcessDeleteDialogComponent,
        AuthorAcessDeletePopupComponent,
    ],
    providers: [
        AuthorAcessService,
        AuthorAcessPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JHipsterBlogAuthorAcessModule {}
