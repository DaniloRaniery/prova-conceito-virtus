import { BaseEntity } from './../../shared';

export class AuthorAcess implements BaseEntity {
    constructor(
        public id?: number,
        public requestLogin?: string,
    ) {
    }
}
