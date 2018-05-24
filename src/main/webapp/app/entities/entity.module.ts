import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { JHipsterBlogPostMySuffixModule } from './post-my-suffix/post-my-suffix.module';
import { JHipsterBlogCommentaryMySuffixModule } from './commentary-my-suffix/commentary-my-suffix.module';
import { JHipsterBlogBodyMySuffixModule } from './body-my-suffix/body-my-suffix.module';
import { JHipsterBlogAssetMySuffixModule } from './asset-my-suffix/asset-my-suffix.module';
import { JHipsterBlogAuthorAcessModule } from './author-acess/author-acess.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        JHipsterBlogPostMySuffixModule,
        JHipsterBlogCommentaryMySuffixModule,
        JHipsterBlogBodyMySuffixModule,
        JHipsterBlogAssetMySuffixModule,
        JHipsterBlogAuthorAcessModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JHipsterBlogEntityModule {}
